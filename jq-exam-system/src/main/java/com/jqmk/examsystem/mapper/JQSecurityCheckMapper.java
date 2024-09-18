package com.jqmk.examsystem.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jqmk.examsystem.dto.userProfile.UserBehavior;
import com.jqmk.examsystem.dto.userProfile.UserBehaviorDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName JQSecurityCheckMapper
 * @Author tian
 * @Date 2024/7/15 14:09
 * @Description 金桥副井口安检系统的相关查询接口
 */
@Mapper
@DS("jq")
public interface JQSecurityCheckMapper {

    @Select("select imgFile from employee_info where name = #{username} and employeeId = #{employeeId} order by updatedTime desc limit 0,1")
    String selectImage(String username, String employeeId);

    @Select("SELECT detectTime,helmetStatus,towelStatus,rescuerStatus,shoesStatus,capLampStatus FROM `employee_clock_history` " +
            "where  personName=#{name}  order by detectTime desc limit 7")
    List<UserBehavior> queryBehavior(String employeeId, String name);

    @Select("SELECT detectTime,helmetStatus,towelStatus,rescuerStatus,shoesStatus,capLampStatus FROM `employee_clock_history` " +
            "where  personName=#{name} order by detectTime desc limit 30")
    List<UserBehavior> wearDataNum(String name);

    @Select("SELECT date_format(detectTime,'%Y-%m-%d'),employeeId FROM `employee_clock_history` WHERE personName = #{name} ORDER BY detectTime desc LIMIT 6,1")
    String selectData(String name, String employeeId);

    @Select("SELECT sum(towelStatus=2) as towelNum,sum(helmetStatus=2) as helmetNum,sum(capLampStatus=2) as capNum," +
            "sum(rescuerStatus=2) as rescurNum,sum(shoesStatus=2) as shoesNum FROM (SELECT towelStatus,helmetStatus,capLampStatus,rescuerStatus,shoesStatus " +
            "from employee_clock_history WHERE personName = #{name} order by detectTime desc limit 0,7 ) as a")
    List<Map<String, Object>> selectWearCount(String name, String employeeId);

    @Select("SELECT SUM(towelNum+helmetNum+capNum+rescurNum+shoesNum) FROM ( SELECT sum(towelStatus=2) as towelNum," +
            "sum(helmetStatus=2) as helmetNum,sum(capLampStatus=2) as capNum,sum(rescuerStatus=2) as rescurNum,sum(shoesStatus=2) as shoesNum " +
            "FROM (SELECT towelStatus,helmetStatus,capLampStatus,rescuerStatus,shoesStatus from employee_clock_history " +
            "WHERE personName = #{name} order by detectTime desc limit 0,7 ) as a) as b")
    Integer wrongWearCount(String name);

    @Select("SELECT SUM(towelNum+helmetNum+capNum+rescurNum+shoesNum) FROM ( SELECT sum(towelStatus=0) as towelNum," +
            "sum(helmetStatus=0) as helmetNum,sum(capLampStatus=0) as capNum,sum(rescuerStatus=0) as rescurNum,sum(shoesStatus=0) as shoesNum " +
            "FROM (SELECT towelStatus,helmetStatus,capLampStatus,rescuerStatus,shoesStatus from employee_clock_history " +
            "WHERE personName = #{name} order by detectTime desc limit 0,7 ) as a) as b")
    Integer noWearCount(String name);

    @Select("SELECT SUM(towelNum+helmetNum+capNum+rescurNum+shoesNum) FROM ( SELECT sum(towelStatus=-1) as towelNum," +
            "sum(helmetStatus=-1) as helmetNum,sum(capLampStatus=-1) as capNum,sum(rescuerStatus=-1) as rescurNum,sum(shoesStatus=-1) as shoesNum " +
            "FROM (SELECT towelStatus,helmetStatus,capLampStatus,rescuerStatus,shoesStatus from employee_clock_history " +
            "WHERE personName = #{name} order by detectTime desc limit 0,7 ) as a) as b")
    Integer noDetectCount(String name);
}
