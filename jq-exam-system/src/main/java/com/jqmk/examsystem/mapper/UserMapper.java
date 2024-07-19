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

    @Select("SELECT username FROM user WHERE (#{jobTypes} IS NULL OR job_type in (${jobTypes})) AND (#{deptNames} IS NULL OR `dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR username LIKE '${name}%')")
    List<String> selectByCondition(String deptNames,String jobTypes,String name);
    @Select("SELECT username FROM user WHERE (#{deptNames} IS NULL OR `dept_name` RLIKE #{deptNames}) " +
            "and (#{name} IS NULL OR username LIKE '${name}%')")
    List<String> selectByConditionOR(String deptNames,String name);

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
            "where id_card = #{idCard}")
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

    @Select("select * from user where id_card = #{idCard} and username =#{username} order by create_date desc limit 0,1 ")
    User selectByCardNoAndName(String idCard, String username);
}
