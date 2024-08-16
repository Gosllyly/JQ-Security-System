package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.userProfile.UserBehaviorDto;
import com.jqmk.examsystem.dto.userProfile.UserProfileInfoDto;
import com.jqmk.examsystem.dto.userProfile.UserProfileSelectDto;
import com.jqmk.examsystem.entity.UserProfileInfo;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserProfileService
 * @Author tian
 * @Date 2024/7/15 13:54
 * @Description 用户画像方法
 */
public interface UserProfileService extends IService<UserProfileInfo> {
    void storeData(JSONObject json);

    Map<String, Object> updateQueryResult(String includeDeptCodes,String names);

    List<UserProfileInfoDto> selectHighPeople(String names);

    List<UserProfileInfoDto> selectMediumPeople(String names);

    Map<String, Object> selectLowPeople(String names, Integer page, Integer pageSize);

    List<UserBehaviorDto> selectBehavior(String employeeId, String name);

    Map<String, Object> selectByCondition(String creatTime, String names, Long page, Long pageSize);

    Map<String, Object> viewMain();

    Map<String, Object> violationCount(String name, String employeeId);

    Map<String, Object> tableData(String name, String employeeId);
}
