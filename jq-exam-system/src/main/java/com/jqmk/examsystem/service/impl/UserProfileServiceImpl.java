package com.jqmk.examsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.userProfile.*;
import com.jqmk.examsystem.entity.PenaltyData;
import com.jqmk.examsystem.entity.UserProfileInfo;
import com.jqmk.examsystem.errors.ErrorCodeEnum;
import com.jqmk.examsystem.exception.BizException;
import com.jqmk.examsystem.mapper.JQSecurityCheckMapper;
import com.jqmk.examsystem.mapper.UserProfileMapper;
import com.jqmk.examsystem.service.UserProfileService;
import com.jqmk.examsystem.utils.StringsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * @ClassName UserProfileServiceImpl
 * @Author tian
 * @Date 2024/7/15 13:57
 * @Description 用户画像方法实现
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfileInfo> implements UserProfileService {

    @Resource
    private JQSecurityCheckMapper jqSecurityCheckMapper;
    @Resource
    private UserProfileMapper userProfileMapper;

    @Override
    public void storeData(JSONObject json) {

        JSONArray result = json.getJSONArray("data");
        for (int i = 0; i < result.size(); i++) {
            UserProfileInfo userProfileInfo = new UserProfileInfo();
            userProfileInfo.setEmployeeId(result.getJSONObject(i).getString("employeeId"));
            userProfileInfo.setUsername(result.getJSONObject(i).getString("personName"));
            userProfileInfo.setLevel(result.getJSONObject(i).getString("level"));
            userProfileInfo.setTime(LocalDate.parse(result.getJSONObject(i).getString("date")));
            if (!userProfileInfo.getLevel().equals("低风险")&&!userProfileInfo.getLevel().equals("中风险")&&!userProfileInfo.getLevel().equals("高风险")) {
                userProfileInfo.setLevel("null");
                userProfileInfo.setReason(result.getJSONObject(i).getString("level"));
                save(userProfileInfo);
            }else if (userProfileInfo.getLevel().equals("低风险")){
                userProfileInfo.setScore(100);
                userProfileInfo.setLevel(result.getJSONObject(i).getString("level"));
                save(userProfileInfo);
            }else if (userProfileInfo.getLevel().equals("中风险")){
                userProfileInfo.setScore(90);
                userProfileInfo.setLevel(result.getJSONObject(i).getString("level"));
                save(userProfileInfo);
            }else if (userProfileInfo.getLevel().equals("高风险")){
                userProfileInfo.setScore(75);
                userProfileInfo.setLevel(result.getJSONObject(i).getString("level"));
                save(userProfileInfo);
            }
        }
    }

    @Override
    public Map<String, Object> viewMain() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<UserProfileInfoDto> lowPeople= userProfileMapper.selectLowPeople();
        result.put("low",lowPeople);
        List<UserProfileInfoDto> mediumPeoples= userProfileMapper.selectMediumPeoples();
        result.put("medium",mediumPeoples);
        List<UserProfileInfoDto> highPeoples= userProfileMapper.selectHighPeoples();
        result.put("high",highPeoples);
        return result;
    }

    @Override
    public Map<String, Object> selectByTime(String time) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<String> lowPeople= userProfileMapper.selectLowPeopleByTime(time);
        List<String> mediumPeoples= userProfileMapper.selectMediumPeoplesByTime(time);
        List<String> intersection = (List<String>) CollectionUtils.subtract(mediumPeoples, lowPeople);
        result.put("low",lowPeople);
        result.put("medium",intersection);
        List<String> highPeoples= userProfileMapper.selectHighPeoplesByTime(time);
        result.put("high",highPeoples);
        return result;
    }

    @Override
    public Map<String, Object> violationCount(String name, String employeeId) {
        //String data = jqSecurityCheckMapper.selectData(name,employeeId);
        List<Map<String, Object>> wearDataNum = jqSecurityCheckMapper.selectWearCount(name,employeeId);
        Integer wrongWear = jqSecurityCheckMapper.wrongWearCount(name);
        Integer noWear = jqSecurityCheckMapper.noWearCount(name);
        Integer violationNumber = userProfileMapper.count(name,employeeId);
        Map<String, Object> result = new HashMap<>();
        result.put("violationNumber",violationNumber);//下井违规
        result.put("wearDataNum",wearDataNum);//穿戴违规
        result.put("noWear",noWear);//未穿戴总次数
        result.put("wrongWear",wrongWear);//穿戴不规范总次数
        return result;
    }

    @Override
    public Map<String, Object> resultSort(String name, String employeeId,String level) {
        Integer sort = userProfileMapper.viewResultSort(name,employeeId,level);
        Integer all = userProfileMapper.viewResultAll(level);
        Map<String, Object> result = new HashMap<>();
        result.put("sort",sort);//排名
        result.put("amount",all);//总人数
        return result;
    }

    @Override
    public Map<String, Object> riskPie(String time) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("nowPie",userProfileMapper.riskPie());//排名
//        if (time==null) {
//            result.put("otherPie",userProfileMapper.riskPieYesterday());//默认昨天
//        }else {
//            result.put("otherPie",userProfileMapper.riskPieOtherDay(time));//自定义天数
//        }
        return null;
    }

    @Override
    public List<Map<String, Object>> riskPercentage(String startTime, Integer type) {
        if (type==1) {//7
            return userProfileMapper.riskPercentage(startTime);
        }else if (type==2) {//31
            return userProfileMapper.riskPercentageByWeek(startTime);
        }else if (type==3) {//30*3
            return userProfileMapper.riskPercentageByMonth(startTime);
        }else {
            return userProfileMapper.riskPercentageByMonth(startTime);
        }
    }

    @Override
    public Map<String, Object> selectByName(String name) {
        String names = String.valueOf(StringsUtil.strAdd(name));
        System.out.println(names);
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<UserProfileInfoDto> lowPeople= userProfileMapper.selectByName(names,"低风险");
        result.put("low",lowPeople);
        List<UserProfileInfoDto> mediumPeoples= userProfileMapper.selectByName(names,"中风险");
        result.put("medium",mediumPeoples);
        List<UserProfileInfoDto> highPeoples= userProfileMapper.selectByName(names,"高风险");
        result.put("high",highPeoples);
        return result;
    }

    @Override
    public Map<String, Object> tableData(String name, String employeeId) {
        Map<String, Object> result = new HashMap<>();
        List<UserBehavior> dataList = jqSecurityCheckMapper.wearDataNum(name);
        List<UserBehaviorDto> queryBehaviorList = new ArrayList<>();
        for (UserBehavior ech : dataList) {
            UserBehaviorDto userBehaviorDto = new UserBehaviorDto();
            userBehaviorDto.setHelmetStatus(convert(ech.getHelmetStatus()));
            userBehaviorDto.setTowelStatus(convert(ech.getTowelStatus()));
            userBehaviorDto.setCapLampStatus(convert(ech.getCapLampStatus()));
            userBehaviorDto.setShoesStatus(convert(ech.getShoesStatus()));
            userBehaviorDto.setRescuerStatus(convert(ech.getRescuerStatus()));
            userBehaviorDto.setDetectTime(ech.getDetectTime());
            queryBehaviorList.add(userBehaviorDto);
        }
        // 如何查询数据为空，抛出未知错误
        if (dataList.size() == 0) {
            return null;
        }
        List<PenaltyData> violationData = userProfileMapper.selectViolationData(name);
        result.put("violationData",violationData);//下井违规
        result.put("wearData",queryBehaviorList);//穿戴违规
        return result;
    }

    @Override
    public Map<String, Object> updateQueryResult(String includeDeptCodes,String names) {
        String deptNames = StringsUtil.arrayToString(includeDeptCodes);
        if (names==null) {
            List<String> usernames= userProfileMapper.selectByCondition(deptNames);
            Map<String, Object> result = new HashMap<>();
            result.put("all",usernames);
            return result;
        }else {
            String name = StringsUtil.strTolist(names);
            List<String> usernames= userProfileMapper.selectByCondition(deptNames);
            List<String> nameList= Arrays.asList(name.split(","));
            List<String> intersection = (List<String>) CollectionUtils.intersection(usernames, nameList);
            Map<String, Object> result = new HashMap<>();
            result.put("all",usernames);
            result.put("selected",intersection);
            return result;
        }
    }

    @Override
    public List<UserProfileInfoDto> selectHighPeople(String names) {
        if (names!=null) {
            String name = String.valueOf(StringsUtil.strAdd(names));
            return userProfileMapper.selectHighPeoplesByNames(name);
        }else {
            return userProfileMapper.selectHighPeoples();
        }
    }

    @Override
    public List<UserProfileInfoDto> selectMediumPeople(String names) {
        if (names!=null) {
            String name = String.valueOf(StringsUtil.strAdd(names));
            return userProfileMapper.selectMediumPeoplesByNames(name);
        }else {
            return userProfileMapper.selectMediumPeoples();
        }
    }

    @Override
    public Map<String, Object> selectLowPeople(String names, Integer page, Integer pageSize) {
        if (names!=null) {
            String name = String.valueOf(StringsUtil.strAdd(names));
            List<UserProfileInfoDto> userProfileInfoDtos = userProfileMapper.selectLowPeoplesByNames(name,(page - 1) * pageSize,pageSize);
            Integer total = userProfileMapper.countLowCondition(name);
            Map<String, Object> res = new HashMap();
            res.put("data", userProfileInfoDtos);
            res.put("total", total);
            return res;
        }else {
            List<UserProfileInfoDto> userProfileInfoDtoList = userProfileMapper.selectLowPeoples((page - 1) * pageSize,pageSize);
            Integer total = userProfileMapper.countLow();
            Map<String, Object> res = new HashMap();
            res.put("data", userProfileInfoDtoList);
            res.put("total", total);
            return res;
        }
    }

    @Override
    public Map<String, Object> selectByCondition(String creatTime, String names, Long page, Long pageSize) {
        if (names!=null) {
            String name = String.valueOf(StringsUtil.strAdd(names));
            List<UserProfileDetailDto> userProfileDetailDtos = userProfileMapper.selectByTimeOrName(creatTime,name,(page - 1) * pageSize,pageSize);
            Integer total = userProfileMapper.countWithName(creatTime,name);
            Map<String, Object> res = new HashMap();
            res.put("data", userProfileDetailDtos);
            res.put("total", total);
            return res;
        }else {
            List<UserProfileDetailDto> userProfileDetailDtoList = userProfileMapper.selectByTime(creatTime,(page - 1) * pageSize,pageSize);
            Integer total = userProfileMapper.countWithTime(creatTime);
            Map<String, Object> res = new HashMap();
            res.put("data", userProfileDetailDtoList);
            res.put("total", total);
            return res;
        }
    }

    @Override
    public List<UserBehaviorDto> selectBehavior(String employeeId, String name) {
        List<UserBehavior> dataList = jqSecurityCheckMapper.queryBehavior(employeeId,name);
        List<UserBehaviorDto> queryBehaviorList = new ArrayList<>();
        for (UserBehavior ech : dataList) {
            UserBehaviorDto userBehaviorDto = new UserBehaviorDto();
            userBehaviorDto.setHelmetStatus(convert(ech.getHelmetStatus()));
            userBehaviorDto.setTowelStatus(convert(ech.getTowelStatus()));
            userBehaviorDto.setCapLampStatus(convert(ech.getCapLampStatus()));
            userBehaviorDto.setShoesStatus(convert(ech.getShoesStatus()));
            userBehaviorDto.setRescuerStatus(convert(ech.getRescuerStatus()));
            userBehaviorDto.setDetectTime(ech.getDetectTime());
            queryBehaviorList.add(userBehaviorDto);
        }
        // 如何查询数据为空，抛出未知错误
        if (dataList.size() == 0) {
            return null;
        }
        return queryBehaviorList;
    }

    public String convert(Integer status) {
        if (status == 0) {
            return "未检测";
        } else if (status == 1) {
            return "已穿戴";
        } else if (status == 2) {
            return "穿戴位置错误";
        } else if (status == -1) {
            return "未检测";
        }
        return null;
    }
}
