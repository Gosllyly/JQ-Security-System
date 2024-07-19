package com.jqmk.examsystem.controller;


import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.dto.userProfile.UserProfileSelectDto;
import com.jqmk.examsystem.service.UserProfileService;
import net.sf.json.JSONObject;
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
@RequestMapping("/portrait")
public class UserProfileController {

    @Resource
    private UserProfileService userProfileService;

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

}
