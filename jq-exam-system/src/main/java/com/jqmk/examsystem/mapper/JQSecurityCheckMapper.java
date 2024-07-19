package com.jqmk.examsystem.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jqmk.examsystem.dto.userProfile.UserBehavior;
import com.jqmk.examsystem.dto.userProfile.UserBehaviorDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("SELECT detectTime,helmetStatus,towelStatus,rescuerStatus,shoesStatus,capLampStatus FROM `employee_clock_history_copy` " +
            "where  personName=#{name} and employeeId=#{employeeId} order by detectTime desc limit 7")
    List<UserBehavior> queryBehavior(String employeeId, String name);
}
