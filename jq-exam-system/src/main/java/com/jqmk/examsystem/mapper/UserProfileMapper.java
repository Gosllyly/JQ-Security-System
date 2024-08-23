package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.userProfile.ResultSort;
import com.jqmk.examsystem.dto.userProfile.UserProfileDetailDto;
import com.jqmk.examsystem.dto.userProfile.UserProfileInfoDto;
import com.jqmk.examsystem.entity.PenaltyData;
import com.jqmk.examsystem.entity.UserProfileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
    @Select("SELECT DISTINCT(up.username),user.employee_id,user.img_file FROM `user_profile_data` as up,`user` " +
            "where up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now()) ")
    List<UserProfileInfoDto> selectLowPeople();
    @Select("SELECT COUNT(DISTINCT up.username) FROM `user_profile_data` as up,`user` " +
            "where up.`level`='低风险' and up.username=user.username and to_days(up.creat_time) = to_days(now())")
    Integer countLow();

    @Select("SELECT username,employee_id,level FROM `user_profile_data` where (#{creatTime} IS NULL OR date_format(creat_time,'%Y-%m-%d')=#{creatTime}) and username in (${name}) and level!='null' " +
            "order by field(level,\"高风险\",\"中风险\",\"低风险\") limit #{page},#{pageSize}")
    List<UserProfileDetailDto> selectByTimeOrName(String creatTime, String name, Long page, Long pageSize);

    @Select("SELECT username,employee_id,level FROM `user_profile_data` where date_format(creat_time,'%Y-%m-%d')=#{creatTime} and level!='null' " +
            "order by field(level,\"高风险\",\"中风险\",\"低风险\") limit #{page},#{pageSize}")
    List<UserProfileDetailDto> selectByTime(String creatTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user_profile_data` where (#{creatTime} IS NULL OR date_format(creat_time,'%Y-%m-%d')=#{creatTime}) and username in (${name}) and level!='null' ")
    Integer countWithName(String creatTime, String name);

    @Select("SELECT count(*) FROM `user_profile_data` where date_format(creat_time,'%Y-%m-%d')=#{creatTime} and level!='null' ")
    Integer countWithTime(String creatTime);

    @Select("select distinct username,employee_id from `user_profile_data` where username=#{name} ")
    List<UserProfileInfoDto> selectByName(String name);

    @Select("SELECT DISTINCT(up.username),up.level,up.creat_time,user.employee_id as employeeId,`user`.card_no as cardNo,user.dept_name as deptName,`user`.job_type as jobType,user.img_file as imgFile " +
            "FROM `user_profile_data` as up,`user` where up.username=user.username and `user`.username=#{name} and user.employee_id=#{employeeId} " +
            "order by up.creat_time desc limit 0,1")
    Map<String, Object> selectForDetails(String name,String employeeId);

    @Select("SELECT COUNT(*) FROM `penalty_data` WHERE DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(violation_date) and duty_person=#{name} ")
    Integer count(String name, String employeeId);

    @Select("select violation_date, violation_facts,penalty_amount from penalty_data where duty_person=#{name} order by violation_date desc limit 4")
    List<PenaltyData> selectViolationData(String name);

    @Select("select count(*) from user_profile_data where to_days(creat_time) = to_days(now())")
    Integer countToday();

    @Select("select id,username,employee_id from user_profile_data order by id  limit #{i},1")
    ResultSort selectId(Integer i);

    @Select("UPDATE user_profile_data SET score=score-#{violationNumber} WHERE id = #{id} ")
    void deductPoints(Long id, Integer violationNumber);

    @Select("SELECT `rank`" +
            "FROM (" +
            "    SELECT username, score,level,creat_time, ROW_NUMBER() OVER (ORDER BY score DESC) AS `rank` " +
            "    FROM user_profile_data " +
            ") AS ranked_table " +
            "WHERE username = #{name} and `level`=#{level} and to_days(creat_time) = to_days(now()) order by creat_time desc limit 0,1")
    Integer viewResultSort(String name, String employeeId,String level);

    @Select("select count(*) from user_profile_data WHERE `level`=#{level} and to_days(creat_time) = to_days(now()) ")
    Integer viewResultAll(String level);

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low FROM `user_profile_data` WHERE DATE(creat_time) = CURDATE()")
    Map<String, Object> riskPie();

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low FROM `user_profile_data` WHERE TO_DAYS(NOW()) - TO_DAYS(DATE(creat_time)) = 1")
    Map<String, Object> riskPieYesterday();

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low FROM `user_profile_data` WHERE DATE(creat_time) = #{time}")
    Map<String, Object> riskPieOtherDay(String time);

    @Select("SELECT CONCAT(convert(sum(`level`!='低风险')*100/count(*),decimal(10,2)),'') as percent,DATE(creat_time) as date FROM `user_profile_data` " +
            "WHERE date(creat_time)>=#{startTime} GROUP BY DATE(creat_time) ORDER BY date ")
    List<Map<String, Object>> riskPercentage(String startTime);

    @Select("SELECT CONCAT(convert(sum(`level`!='低风险')*100/count(*),decimal(10,2)),'') as percent,DATE(creat_time) as date FROM `user_profile_data` " +
            "WHERE date(creat_time)>=#{startTime} GROUP BY WEEK(DATE(creat_time)) ORDER BY date ")
    List<Map<String, Object>> riskPercentageByWeek(String startTime);

    @Select("SELECT CONCAT(convert(sum(`level`!='低风险')*100/count(*),decimal(10,2)),'') as percent,DATE(creat_time) as date FROM `user_profile_data` " +
            "WHERE date(creat_time)>=#{startTime} GROUP BY MONTH(DATE(creat_time)) ORDER BY date ")
    List<Map<String, Object>> riskPercentageByMonth(String startTime);

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low ,`user`.dept_name as deptName " +
            "FROM `user_profile_data` as up,`user` WHERE up.username=`user`.username and DATE(up.creat_time) = #{time} " +
            "GROUP BY user.dept_name ORDER BY low desc LIMIT 10")
    List<Map<String, Object>> riskHistogram(String time);

    @Select("SELECT p.duty_person as dutyPerson,p.duty_unit as dutyUnit,count(p.duty_person) as number,up.`level`,SUM(p.penalty_amount) as money,user.employee_id as employeeId " +
            "FROM penalty_data as p,user,user_profile_data as up where p.duty_person=user.username and p.duty_unit=user.dept_name " +
            "and up.username=p.duty_person and (#{time} IS NULL OR p.violation_date=#{time}) and (#{deptName} IS NULL OR p.duty_unit = #{deptName}) GROUP BY p.duty_person,user.username ")
    List<Map<String, Object>> violationData(String time,String deptName);

    @Select("SELECT distinct  username,employee_id,level FROM `user_profile_data` where to_days(creat_time) = to_days(now()) and username in (${names}) " +
            "and level!='null' and level=#{type}")
    List<UserProfileInfoDto> selectByName(String names,String type);
}
