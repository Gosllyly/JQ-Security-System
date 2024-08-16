package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.mapper.UserMapper;
import com.jqmk.examsystem.service.UserService;
import com.jqmk.examsystem.utils.JwtUtil;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表，存储用户基本信息，根据网页，是通过身份证和姓名进行唯一性确认 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Map<String, Object> login(String userName, String password) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",userName);
        User user = userMapper.selectOne(wrapper);
        Map<String, Object> result = new HashMap<>();
        // 当用户名不存在时，返回-1
        if (user == null) {
            result.put("code", -1);
        }
        // 当用户名存在且密码正确时，返回1
        if (password.equals(user.getPassword())) {
            result.put("user", user);
            result.put("code", 1);
            result.put("token", JwtUtil.generateToken(userName));
        } else {
            // 当用户名正确，但密码不正确时，返回0
            result.put("code", 0);
        }
        return result;
    }

    @Override
    public List<String> selectUsersByNames(SelectPeoplesDto selectPeoplesDto) {
        String deptNames = StringsUtil.arrayToString1(selectPeoplesDto.getIncludeDeptCodes().toString());
        if (selectPeoplesDto.getIncludeJobType()==null) {
            List<String> userList = userMapper.selectByConditionOR(deptNames,selectPeoplesDto.getName());
            return userList;
        }else {
            String jobTypes = StringsUtil.arrayToStr(selectPeoplesDto.getIncludeJobType().toString());
            System.out.println(jobTypes);
            List<String> userList = userMapper.selectByCondition(deptNames,jobTypes,selectPeoplesDto.getName());
            return userList;
        }
    }
}
