package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户表，存储用户基本信息，根据网页，是通过身份证和姓名进行唯一性确认 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT username FROM user WHERE job_type in (${jobTypes}) AND (#{deptNames} IS NULL OR `dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR username LIKE '${name}%')")
    List<String> selectByCondition(String deptNames,String jobTypes,String name);
    @Select("SELECT username FROM user WHERE (#{deptNames} IS NULL OR `dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR username LIKE '${name}%')")
    List<String> selectByConditionOR(String deptNames,String name);

    @Select("SELECT distinct user.username FROM user,user_profile_data_dispose as up WHERE (#{deptNames} IS NULL OR user.`dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR user.username LIKE '${name}%') and up.level = #{riskPeople} and to_days(up.creat_time) = to_days(now()) and user.username=up.username")
    List<String> selectByConditionOther(String deptNames,String name,String riskPeople);

    @Select("SELECT distinct user.username FROM user,user_profile_data_dispose as up WHERE user.job_type in (${jobTypes}) AND (#{deptNames} IS NULL OR user.`dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR user.username LIKE '${name}%') and up.level = #{riskPeople} and to_days(up.creat_time) = to_days(now()) and user.username=up.username")
    List<String> selectByConditionAll(String deptNames,String jobTypes, String name,String riskPeople);

    @Update("UPDATE user " +
            "set " +
            "username = #{username}," +
            "dept_name = #{deptName}," +
            "id_card = #{idCard}," +
            "card_no = #{cardNo}," +
            "job_type = #{jobType}," +
            "employee_id = #{employeeId}," +
            "img_file = #{imgFile}," +
            "create_date = #{createDate} " +
            "where id_card = #{idCard} and username =#{username}")
    void updateUser(String username, String deptName, String idCard, Integer cardNo, String employeeId, String jobType,String imgFile, LocalDateTime createDate);

    @Insert("INSERT INTO user (username,dept_name,id_card,card_no,employee_id,job_type,img_file,create_date) " +
            "VALUES (" +
            "#{user.username}, " +
            "#{user.deptName}, " +
            "#{user.idCard}, " +
            "#{user.cardNo}, " +
            "#{user.employeeId}, " +
            "#{user.jobType}, " +
            "#{user.imgFile}, " +
            "#{user.createDate}) ")
    void insertUser(@Param("user") User user);

    @Select("select count(*) from user where id_card = #{idCard} and username =#{username} order by create_date desc limit 0,1 ")
    Integer selectByCardNoAndName(String idCard, String username);

    @Update("<script>" +
            "UPDATE user " +
            "SET role_id = #{roleId} " +
            "WHERE user.username IN " +
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>" +
            "#{name}" +
            "</foreach>" +
            "</script>")
    void updateRoleIdByNames(@Param("names") List<String> names, @Param("roleId") String roleId);

}
