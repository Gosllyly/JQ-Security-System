package com.jqmk.examsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName QuestionBaseBankController
 * @Author tian
 * @Date 2024/8/16 10:04
 * @Description 基础试题库界面接口
 */
@RestController
@RequestMapping("/baseBank")
public class QuestionBaseBankController {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionMapper questionMapper;


    @GetMapping("/main")
    public WebResult viewQuestionBank(@RequestParam Long page, @RequestParam Long pageSize){
        Page records = questionService.selectBaseBankByPage(page,pageSize);
        return WebResult.ok().data(records);
    }

    @GetMapping("/selectQuestion")
    public WebResult selectQuestion(@Nullable String stem, @Nullable Integer type, @RequestParam Long page, @RequestParam Long pageSize){
        Page queryPage = questionService.queryQuestionPage(stem,type,page,pageSize);
        return WebResult.ok().data(queryPage);
    }

    @GetMapping("/questionCount")
    public WebResult questionCount(){
        return WebResult.ok().data(questionMapper.countQuestion());
    }

    @GetMapping("/viewDel")
    public WebResult viewDelQuestion(@RequestParam Long page, @RequestParam Long pageSize){
        return WebResult.ok().data(questionService.viewDelQuestion(page,pageSize));
    }

    @PostMapping ("/restore")
    public WebResult restoreQuestion(@RequestParam String ids){
        questionService.restoreQuestion(ids);
        return WebResult.ok().message("恢复成功");
    }

}
