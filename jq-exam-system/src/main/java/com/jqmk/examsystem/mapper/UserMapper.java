package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT username FROM user WHERE (#{deptNames} IS NULL OR dept_name RLIKE #{deptNames}) " +
            "and (#{jobTypes} IS NULL OR job_type RLIKE #{jobTypes}) and (#{name} IS NULL OR username like '${name}%')")
    List<String> selectByCondition(String deptNames,String jobTypes,String name);
}
