package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表，存储用户基本信息，根据网页，是通过身份证和姓名进行唯一性确认 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface UserService extends IService<User> {

    Map<String, Object> login(String userName, String password);

    List<String> selectUsersByNames(SelectPeoplesDto selectPeoplesDto);
}
