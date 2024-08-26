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
            "where  personName=#{name} and employeeId=#{employeeId} order by detectTime desc limit 7")
    List<UserBehavior> queryBehavior(String employeeId, String name);

    @Select("SELECT detectTime,helmetStatus,towelStatus,rescuerStatus,shoesStatus,capLampStatus FROM `employee_clock_history` " +
            "where  personName=#{name} order by detectTime desc limit 4")
    List<UserBehavior> wearDataNum(String name);

    @Select("SELECT date_format(detectTime,'%Y-%m-%d'),employeeId FROM `employee_clock_history` WHERE personName = #{name} ORDER BY detectTime desc LIMIT 6,1")
    String selectData(String name, String employeeId);

    @Select("SELECT sum(helmetStatus=2) as helmetNum,sum(helmetStatus=2) as helmetNum,sum(towelStatus=2) as towelNum,sum(shoesStatus=2) as shoesNum,sum(capLampStatus=2) as capNum,sum(rescuerStatus=2) as rescurNum " +
            "FROM `employee_clock_history` WHERE personName = #{name} order by detectTime desc limit 0,7 ")
    List<Map<String, Object>> selectWearCount(String name, String employeeId);

    @Select("select count(*) from `employee_clock_history` WHERE personName = #{name} and (helmetStatus=2 or capLampStatus=2 or shoesStatus=2 or towelStatus=2 or rescuerStatus=2) order by detectTime desc limit 0,7 ")
    Integer wrongWearCount(String name);

    @Select("select count(*) from `employee_clock_history` WHERE personName = #{name} and (helmetStatus=0 or capLampStatus=0 or shoesStatus=0 or towelStatus=0 or rescuerStatus=0) order by detectTime desc limit 0,7 ")
    Integer noWearCount(String name);
}
