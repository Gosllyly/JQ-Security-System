package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.ExamCrowdManageDto;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamCrowdManage;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.service.ExamCrowdManageService;
import com.jqmk.examsystem.service.UserService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
    public WebResult addCrowdManage(@RequestBody ExamCrowdManageDto examCrowdManageDto) {
        ExamCrowdManage examCrowdManage = new ExamCrowdManage();
        examCrowdManage.setCrowdName(examCrowdManageDto.getCrowdName());
        examCrowdManage.setIncludeJobType(examCrowdManageDto.getIncludeJobType());
        examCrowdManage.setRiskPeople(examCrowdManageDto.getRiskPeople());
        examCrowdManage.setIncludeDeptCodes(examCrowdManageDto.getIncludeDeptCodes());
        examCrowdManage.setIncludePeoples(StringsUtil.stringRecomNew(examCrowdManageDto.getIncludePeoples().toString()));
        if (examCrowdManageService.count(new QueryWrapper<ExamCrowdManage>().eq("crowd_name",examCrowdManage.getCrowdName()))==0) {
            examCrowdManageService.save(examCrowdManage);
            return WebResult.ok().message("创建考试人群成功");
        }else {
            return WebResult.fail().message("考试人群名称重复");
        }
    }

    @GetMapping("/selectDeptName")
    public WebResult selectDeptName() {
        List<String> deptNameList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getDeptName).eq(User::getDeleteFlag,0), Object::toString);
        Stream<String> distinct = deptNameList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    @GetMapping("/selectJobType")
    public WebResult selectJobType() {
        List<String> jobTypeList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getJobType).eq(User::getDeleteFlag,0), Object::toString);
        Stream<String> distinct = jobTypeList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    @PostMapping ("/selectPeoples")
    public WebResult selectPeoples(@RequestBody SelectPeoplesDto selectPeoplesDto) {
        List<String> users = userService.selectUsersByNames(selectPeoplesDto);
        return WebResult.ok().data(users);
    }

    @PostMapping("/update")
    public WebResult updateCrowdManage(@RequestBody ExamCrowdManageDto examCrowdManageDto) {
        ExamCrowdManage examCrowdManage = new ExamCrowdManage();
        examCrowdManage.setId(examCrowdManageDto.getId());
        examCrowdManage.setCrowdName(examCrowdManageDto.getCrowdName());
        examCrowdManage.setIncludeJobType(examCrowdManageDto.getIncludeJobType());
        examCrowdManage.setRiskPeople(examCrowdManageDto.getRiskPeople());
        examCrowdManage.setIncludeDeptCodes(examCrowdManageDto.getIncludeDeptCodes());
        examCrowdManage.setIncludePeoples(StringsUtil.stringRecomNew(examCrowdManageDto.getIncludePeoples().toString()));
        if (examCrowdManageService.count(new QueryWrapper<ExamCrowdManage>().eq("crowd_name",examCrowdManage.getCrowdName()))<2) {
            examCrowdManage.setUpdateTime(LocalDateTime.now());
            examCrowdManageService.updateById(examCrowdManage);
            return WebResult.ok().message("更新成功");
        }else {
            return WebResult.fail().message("考试人群名称重复");
        }
    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody ExamCrowdManage examCrowdManage) {
        examCrowdManageService.removeById(examCrowdManage);
        return WebResult.ok().message("删除成功");
    }

    @GetMapping("/selectCrowd")
    public WebResult selectCrowd(@RequestParam String crowdName) {
        return WebResult.ok().data(examCrowdManageService.list(new QueryWrapper<ExamCrowdManage>()
                .select()
                .eq("deleted",0)
                .like(crowdName != null, "crowd_name", crowdName)));
    }
}
