package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.UserRoleDto;
import com.jqmk.examsystem.entity.RoleManage;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.mapper.UserMapper;
import com.jqmk.examsystem.mapper.UserProfileMapper;
import com.jqmk.examsystem.service.RoleManageService;
import com.jqmk.examsystem.service.UserProfileService;
import com.jqmk.examsystem.service.UserService;
import com.jqmk.examsystem.utils.JwtUtil;
import com.jqmk.examsystem.utils.StringsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
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
    public UserService userService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleManageService roleManageService;


    @Resource
    private UserProfileMapper userProfileMapper;

    @Resource
    private UserProfileService userProfileService;

    @Override
    public Map<String, Object> login(String userName, String password) {
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("username",userName);
//        User user = userMapper.selectOne(wrapper);
        UserRoleDto user = userMapper.selectUserAndRole(userName);
        Map<String, Object> result = new HashMap<>();
        // 当用户名不存在时，返回-1
        if (user == null) {
            result.put("code", -1);
        }
        // 当用户名存在且密码正确时，返回1
        if (password.equals(user.getPassword())) {
//            String authUrls = roleManageService.getById(user.getId()).getAuthUrls();
//            user.setAuthUrls(authUrls);
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
    public Map<String, Object> selectInfo(String userName){
        UserRoleDto user = userMapper.selectUserAndRole(userName);
        Map<String, Object> result = new HashMap<>();
        // 当用户名不存在时，返回-1
        if (user == null) {
            result.put("code", -1);
            return result;
        }

        result.put("user", user);
        result.put("code", 1);
        result.put("token", JwtUtil.generateToken(userName));

        return result;
    }
    @Override
    public List<String> selectUsersByNames(SelectPeoplesDto selectPeoplesDto) {
        if (selectPeoplesDto.getIncludeJobType()==null&&selectPeoplesDto.getTime()==null) {
            String deptNames = StringsUtil.arrayToString1(selectPeoplesDto.getIncludeDeptCodes());
            List<String> userList1 = userMapper.selectByConditionOR(deptNames,selectPeoplesDto.getName());
            if (selectPeoplesDto.getType()!=null) {
                List<String> userList = (List<String>) CollectionUtils.intersection(userList1, userProfileMapper.violationPeople(selectPeoplesDto.getType()));
                return userList;
            }
            return userList1;
        }else if (selectPeoplesDto.getIncludeJobType()!=null&&selectPeoplesDto.getTime()==null){
            String deptNames = StringsUtil.arrayToString1(selectPeoplesDto.getIncludeDeptCodes());
            String jobTypes = StringsUtil.arrayToStr(selectPeoplesDto.getIncludeJobType().toString());
            List<String> userList1 = userMapper.selectByCondition(deptNames,jobTypes,selectPeoplesDto.getName());
            if (selectPeoplesDto.getType()!=null) {
                List<String> userList = (List<String>) CollectionUtils.intersection(userList1, userProfileMapper.violationPeople(selectPeoplesDto.getType()));
                return userList;
            }
            return userList1;
        }else if (selectPeoplesDto.getIncludeJobType()==null&&selectPeoplesDto.getTime()!=null){
            String deptNames = StringsUtil.arrayToString1(selectPeoplesDto.getIncludeDeptCodes());
            List<String> userList1 = userMapper.selectByConditionOther(deptNames,selectPeoplesDto.getName());
            List<String> userList2 = (List<String>) CollectionUtils.intersection(userList1, (Collection) userProfileService.selectByTime(selectPeoplesDto.getTime()).get(selectPeoplesDto.getRisk()));
            if (selectPeoplesDto.getType()!=null) {
                List<String> userList = (List<String>) CollectionUtils.intersection(userList2, userProfileMapper.violationPeople(selectPeoplesDto.getType()));
                return userList;
            }
            return userList2;
        }else {
            String jobTypes = StringsUtil.arrayToStr(selectPeoplesDto.getIncludeJobType().toString());
            String deptNames = StringsUtil.arrayToString1(selectPeoplesDto.getIncludeDeptCodes());
            //String riskPeople = StringsUtil.arrayToStr(selectPeoplesDto.getRiskPeople().toString());
            List<String> userList1 = userMapper.selectByConditionAll(deptNames,jobTypes,selectPeoplesDto.getName());
            List<String> userList2 = (List<String>) CollectionUtils.intersection(userList1,  (Collection) userProfileService.selectByTime(selectPeoplesDto.getTime()).get(selectPeoplesDto.getRisk()));
            if (selectPeoplesDto.getType()!=null) {
                List<String> userList = (List<String>) CollectionUtils.intersection(userList2, userProfileMapper.violationPeople(selectPeoplesDto.getType()));
                return userList;
            }
            return userList2;
        }
    }

    @Override
    public void updateUserTable(List<String> includePeopleList, String roleId) {
        userMapper.updateRoleIdByNames(includePeopleList,roleId);
    }

    @Override
    public UserRoleDto getByName(String name) {
        return userMapper.selectUserAndRole(name);
    }

    @Override
    public RoleManage getRoleByRoleId(String roleId){
        return roleManageService.getById(roleId);
    };

}
