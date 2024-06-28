package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamCrowdManage;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.service.ExamCrowdManageService;
import com.jqmk.examsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 考试人群管理界面 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/examCrowd")
public class ExamCrowdManageController {

    @Resource
    private ExamCrowdManageService examCrowdManageService;
    @Resource
    private UserService userService;

    @GetMapping("/main")
    public WebResult viewMain(@RequestParam Long page, @RequestParam Long pageSize) {
        Page<ExamCrowdManage> questionPage = new Page<>(page,pageSize);
        Page examCrowdManages =  questionPage.setRecords(examCrowdManageService.page(questionPage,new QueryWrapper<ExamCrowdManage>()
                .orderByDesc("update_time")).getRecords());
        return WebResult.ok().data(examCrowdManages);
    }

    @PostMapping("/add")
    public WebResult addCrowdManage(@RequestBody ExamCrowdManage examCrowdManage) {
        examCrowdManageService.save(examCrowdManage);
        return WebResult.ok().message("创建考试人群成功");
    }

    @GetMapping("/selectDeptName")
    public WebResult selectDeptName() {
        List<String> deptNameList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getDeptName).eq(User::getDeleteFlag,0), Object::toString);
        return WebResult.ok().data(deptNameList);
    }

    @GetMapping("/selectJobType")
    public WebResult selectJobType() {
        List<String> jobTypeList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getJobType).eq(User::getDeleteFlag,0), Object::toString);
        return WebResult.ok().data(jobTypeList);
    }

    @GetMapping("/selectPeoples")
    public WebResult selectPeoples(String includeDeptCodes, String includeJobType, String name) {
        List<String> users = userService.selectUsersByNames(includeDeptCodes,includeJobType,name);
        return WebResult.ok().data(users);
    }

    @PostMapping("/update")
    public WebResult updateCrowdManage(@RequestBody ExamCrowdManage examCrowdManage) {
        examCrowdManage.setUpdateTime(LocalDateTime.now());
        examCrowdManageService.updateById(examCrowdManage);
        return WebResult.ok().message("更新成功");
    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody ExamCrowdManage examCrowdManage) {
        examCrowdManageService.removeById(examCrowdManage);
        return WebResult.ok().message("删除成功");
    }

    @GetMapping("/selectCrowd")
    public WebResult selectCrowd(@RequestParam String crowdName) {
        return WebResult.ok().data(examCrowdManageService.getMap(new QueryWrapper<ExamCrowdManage>()
                .select("crowd_name","include_peoples").like(crowdName != null, "crowd_name", crowdName)));
    }

}
