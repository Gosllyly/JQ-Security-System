package com.jqmk.examsystem.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.dto.userProfile.UserProfileDetailDto;
import com.jqmk.examsystem.entity.UserProfileInfo;
import com.jqmk.examsystem.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserProfileDetailController
 * @Author tian
 * @Date 2024/7/19 8:41
 * @Description 用户画像详情界面接口
 */
@RestController
@RequestMapping("/profileDetail")
public class UserProfileDetailController {


    @Resource
    private UserProfileService userProfileService;


    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/main")
    public WebResult mainView(@RequestParam Long page, @RequestParam Long pageSize) {
        Page<UserProfileInfo> userProfileInfoPage = new Page<>(page,pageSize);
        Page profileInfoPage =  userProfileInfoPage.setRecords(userProfileService.page(userProfileInfoPage,new QueryWrapper<UserProfileInfo>()
                .select("username","employee_id","level")
                .ge("creat_time",LocalDate.now())
                .notLike("level","null")
                .last("order by field(level,\"高风险\",\"中风险\",\"低风险\")")).getRecords());
        profileInfoPage.setRecords(BeanUtil.copyToList(profileInfoPage.getRecords(), UserProfileDetailDto.class));
        return WebResult.ok().data(profileInfoPage);
    }

    @PostMapping("/select")
    public WebResult selectView(String creatTime, String names, Long page, Long pageSize) {
        return WebResult.ok().data(userProfileService.selectByCondition(creatTime,names,page,pageSize));
    }

    @GetMapping("/behavior")
    public WebResult behavior(@RequestParam String employeeId, @RequestParam String name) {
        return WebResult.ok().data(userProfileService.selectBehavior(employeeId,name));
    }
}
