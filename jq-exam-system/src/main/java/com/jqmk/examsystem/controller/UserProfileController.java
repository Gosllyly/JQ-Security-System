package com.jqmk.examsystem.controller;


import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.dto.userProfile.UserProfileSelectDto;
import com.jqmk.examsystem.mapper.UserProfileMapper;
import com.jqmk.examsystem.service.UserProfileService;
import lombok.extern.log4j.Log4j2;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserProfileController
 * @Author tian
 * @Date 2024/7/15 13:52
 * @Description 用户画像相关接口
 */
@RestController
@Log4j2
@RequestMapping("/portrait")
public class UserProfileController {

    @Resource
    private UserProfileService userProfileService;
    @Resource
    private UserProfileMapper userProfileMapper;

    @PostMapping("/data")
    public String getData(@RequestBody JSONObject json) {
        userProfileService.storeData(json);
        return "success";
    }

    @PostMapping("/mainHigh")
    public WebResult mainHighView(String names) {
        return WebResult.ok().data(userProfileService.selectHighPeople(names));
    }

    @PostMapping("/mainMedium")
    public WebResult mainMediumView(String names) {
        return WebResult.ok().data(userProfileService.selectMediumPeople(names));
    }

    @PostMapping("/mainLow")
    public WebResult mainLowView(String names,Integer page,Integer pageSize) {
        return WebResult.ok().data(userProfileService.selectLowPeople(names,page,pageSize));
    }

    @PostMapping ("/updateQuery")
    public WebResult updateQueryResult(String includeDeptCodes,String names) {
        return WebResult.ok().data(userProfileService.updateQueryResult(includeDeptCodes,names));
    }

    /**
     * 下面部分开始是二期用户画像接口
     */
    @GetMapping("main")
    public WebResult mainView() {
        return WebResult.ok().data(userProfileService.viewMain());
    }

    @GetMapping("selectByTime")
    public WebResult selectByTime(String time) {
        return WebResult.ok().data(userProfileService.selectByTime(time));
    }

    @PostMapping("select")
    public WebResult selectByName(@RequestBody SelectPeoplesDto selectPeoplesDto) {
        return WebResult.ok().data(userProfileService.selectByName(selectPeoplesDto.getName()));
    }

    @GetMapping("details")
    public WebResult selectByName(String name,String employeeId) {
        return WebResult.ok().data(userProfileMapper.selectForDetails(name,employeeId));
    }

    @GetMapping("violationData")
    public WebResult violationCount(String name,String employeeId) {
        return WebResult.ok().data(userProfileService.violationCount(name,employeeId));
    }

    @GetMapping("violationPeople")
    public WebResult violationPeople(String type) {
        return WebResult.ok().data(userProfileMapper.violationPeople(type));
    }

    @GetMapping("violationType")
    public WebResult violationType() {
        return WebResult.ok().data(userProfileMapper.violationType());
    }

    @GetMapping("tableData")
    public WebResult tableData(String name,String employeeId) {
        return WebResult.ok().data(userProfileService.tableData(name,employeeId));
    }

    @GetMapping("sort")
    public WebResult resultSort(String name,String employeeId,String level) {
        return WebResult.ok().data(userProfileService.resultSort(name,employeeId,level));
    }


}
