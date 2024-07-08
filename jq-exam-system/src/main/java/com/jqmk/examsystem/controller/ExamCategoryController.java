package com.jqmk.examsystem.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.ExamCategoryDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamCategory;
import com.jqmk.examsystem.entity.QuestionBank;
import com.jqmk.examsystem.mapper.ExamCategoryMapper;
import com.jqmk.examsystem.service.ExamCategoryService;
import com.jqmk.examsystem.service.QuestionBankService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 类别管理界面 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/category")
public class ExamCategoryController {
    @Resource
    private ExamCategoryService examCategoryService;
    @Resource
    private ExamCategoryMapper examCategoryMapper;
    @Resource
    private QuestionBankService questionBankService;


    @GetMapping("/main")
    public WebResult viewMain() {
        List<ExamCategory> examCategoryList = examCategoryMapper.selectList(new QueryWrapper<ExamCategory>().select("id","category_name","parent_id")
                        .groupBy("id","parent_id").eq("deleted", 0).orderByDesc("update_time"));
        return WebResult.ok().data(BeanUtil.copyToList(examCategoryList, ExamCategoryDto.class));
    }

    @GetMapping("/bankName")
    public WebResult viewBankName(@RequestParam(required = false) String bankName) {
        List<QuestionBank> questionBankList = questionBankService.list(
                new QueryWrapper<QuestionBank>().select("id","bank_name").like(bankName != null, "bank_name", bankName)
                        .orderByDesc("update_time"));
        return WebResult.ok().data(questionBankList);
    }

    @PostMapping("/add")
    @Transactional
    public WebResult addCategory(@RequestBody ExamCategory examCategory) {
        examCategoryService.save(examCategory);
        return WebResult.ok().message("添加成功");
    }

    @PostMapping("/update")
    @Transactional
    public WebResult updateCategory(@RequestBody ExamCategory examCategory) {
        examCategory.setUpdateTime(LocalDateTime.now());
        examCategoryService.updateById(examCategory);
        return WebResult.ok().message("更新成功");
    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody ExamCategory examCategory) {
        examCategoryService.removeById(examCategory);
        return WebResult.ok().message("删除成功");
    }
}
