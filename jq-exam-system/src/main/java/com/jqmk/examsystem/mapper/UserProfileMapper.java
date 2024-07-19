package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.userProfile.UserProfileDetailDto;
import com.jqmk.examsystem.dto.userProfile.UserProfileInfoDto;
import com.jqmk.examsystem.entity.UserProfileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName UserProfileMapper
 * @Author tian
 * @Date 2024/7/15 13:57
 * @Description 用户画像sql方法
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfileInfo> {

    @Select("SELECT username FROM user WHERE (#{deptNames} IS NULL OR `dept_name` RLIKE #{deptNames})")
    List<String> selectByCondition(String deptNames);

    @Select("select img_file from user where username = #{username} and employee_id = #{employeeId} order by create_date desc limit 0,1")
    String selectImage(String username, String employeeId);

    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "where up.`level`='高风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    List<UserProfileInfoDto> selectHighPeoples();

    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "where up.`level`='中风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    List<UserProfileInfoDto> selectMediumPeoples();

    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "WHERE up.username in (${name}) and up.`level`='高风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    List<UserProfileInfoDto> selectHighPeoplesByNames(String name);

    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "WHERE up.username in (${name}) and up.`level`='中风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    List<UserProfileInfoDto> selectMediumPeoplesByNames(String name);

    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "WHERE up.username in (${name}) and up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now()) limit #{page},#{pageSize}")
    List<UserProfileInfoDto> selectLowPeoplesByNames(String name, Integer page, Integer pageSize);

    @Select("SELECT COUNT(DISTINCT up.username) FROM `user_profile_data` as up,`user` " +
            "WHERE up.username in (${name}) and up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    Integer countLowCondition(String names);
    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "where up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now()) limit #{page},#{pageSize}")
    List<UserProfileInfoDto> selectLowPeoples(Integer page, Integer pageSize);

    @Select("SELECT COUNT(DISTINCT up.username) FROM `user_profile_data` as up,`user` " +
            "where up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    Integer countLow();

    @Select("SELECT username,employee_id,level FROM `user_profile_data` where (#{creatTime} IS NULL OR date_format(creat_time,'%Y-%m-%d')=#{creatTime}) and username in (${name}) and level!='数据不足' " +
            "order by field(level,\"高风险\",\"中风险\",\"低风险\") limit #{page},#{pageSize}")
    List<UserProfileDetailDto> selectByTimeOrName(String creatTime, String name, Long page, Long pageSize);

    @Select("SELECT username,employee_id,level FROM `user_profile_data` where date_format(creat_time,'%Y-%m-%d')=#{creatTime} and level!='数据不足' " +
            "order by field(level,\"高风险\",\"中风险\",\"低风险\") limit #{page},#{pageSize}")
    List<UserProfileDetailDto> selectByTime(String creatTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user_profile_data` where (#{creatTime} IS NULL OR date_format(creat_time,'%Y-%m-%d')=#{creatTime}) and username in (${name}) and level!='数据不足' ")
    Integer countWithName(String creatTime, String name);

    @Select("SELECT count(*) FROM `user_profile_data` where date_format(creat_time,'%Y-%m-%d')=#{creatTime} and level!='数据不足' ")
    Integer countWithTime(String creatTime);
}
