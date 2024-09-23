package com.jqmk.examsystem.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.QuestionBank;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionBankService;
import com.jqmk.examsystem.service.QuestionService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题库界面 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/questionbank")
public class QuestionBankController {

    @Resource
    private QuestionBankService questionBankService;
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionMapper questionMapper;


    @GetMapping("/main")
    public WebResult viewMain() {
        List<QuestionBank> questionBankList = questionBankService.list(
                new QueryWrapper<QuestionBank>().eq("status",0).orderByDesc("update_time"));
        return WebResult.ok().data(questionBankList);
    }
    @GetMapping("/queryBankName")
    public WebResult queryBankName(@RequestParam String bankName) {
        List<QuestionBank> questionBankList = questionBankService.list(
                new QueryWrapper<QuestionBank>().like("bank_name",bankName).eq("status",0).orderByDesc("update_time"));
        return WebResult.ok().data(questionBankList);
    }

    @PostMapping("/createBankName")
    public WebResult addBankName(@RequestBody QuestionBank questionBank) {
        if (questionMapper.countBank(questionBank.getBankName())==0) {
            questionBankService.save(questionBank);
            return WebResult.ok().message("创建题库成功");
        }else {
            return WebResult.fail().message("题库名称重复");
        }
    }

    @PostMapping("/updateBankName")
    public WebResult updateBankName(@RequestBody QuestionBank questionBank) {
        if (questionMapper.countBank(questionBank.getBankName())==0) {
            questionBankService.updateById(questionBank);
            return WebResult.ok().message("更新题库名称成功");
        }else {
            return WebResult.fail().message("题库名称重复");
        }
    }

    @DeleteMapping("/deleteBankName")
    public WebResult deleteBankName(@RequestBody QuestionBank questionBank) {
        questionBankService.removeById(questionBank);
        questionMapper.delQuestion(Math.toIntExact(questionBank.getId()));
        return WebResult.ok().message("删除成功");
    }

    @PostMapping("/importQuestionBank")
    public WebResult importExcel(@RequestParam("file") MultipartFile file,@RequestParam Integer questionBankId) throws IOException {
        questionBankService.importData(file,questionBankId);
        return WebResult.ok().message("导入成功");
    }

    @GetMapping ("/exportQuestionBank")
    public void exportQuestionBank(@RequestParam Integer questionBankId, HttpServletResponse response) {
        questionService.selectByBankId(questionBankId,response);
    }
    @PostMapping("/addQuestion")
    public WebResult addQuestion(@RequestBody Question questionInfo) {
        //检查题目是否已经存在
        String stem = questionInfo.getStem();
        JSONObject options = questionInfo.getOptions();
        Integer questionBankId = questionInfo.getQuestionBankId();
        if (questionMapper.countByStemAndOptions(stem, options.toJSONString(),questionBankId) > 0) {
            return WebResult.fail().message("重复添加");
        }
        questionService.save(questionInfo);
        return WebResult.ok().message("添加成功");
    }
    @GetMapping("/addQuestionToBank")
    public WebResult addQuestionToBank(@RequestParam Integer questionBankId, @RequestParam Integer id) {
        ExamQuestion questionInfo = questionMapper.selectQuestionOne(id);
        //检查题目是否已经存在
        String stem = questionInfo.getStem();
        JsonNode options = questionInfo.getOptions();
        if (questionMapper.countByStemAndOptions(stem, String.valueOf(options),questionBankId) > 0) {
            return WebResult.fail().message("重复添加");
        }
        questionMapper.saveQuestion(questionBankId,id);
        return WebResult.ok().message("添加成功");
    }

    @PostMapping("/updateQuestion")
    public WebResult updateQuestion(@RequestBody Question questionInfo) {
        //检查题目是否已经存在
        String stem = questionInfo.getStem();
        JSONObject options = questionInfo.getOptions();
        Integer questionBankId = questionInfo.getQuestionBankId();

        if (questionMapper.countByStemAndOptions(stem, options.toJSONString(),questionBankId) > 0) {
            return WebResult.fail().message("重复添加");
        }
        questionService.updateById(questionInfo);
        return WebResult.ok().message("更新成功");
    }

    @DeleteMapping("/deleteQuestion")
    public WebResult deleteQuestion(@RequestBody Question questionInfo) {
        questionService.removeById(questionInfo);
        return WebResult.ok().message("删除成功");
    }

    @GetMapping("/selectQuestion")
    public WebResult selectQuestion(@RequestParam Integer questionBankId,@Nullable String stem, @Nullable Integer type, @RequestParam Long page, @RequestParam Long pageSize){
        Page queryPage = questionService.selectQueryPage(questionBankId,stem,type,page,pageSize);
        return WebResult.ok().data(queryPage);
    }

    @GetMapping("/viewQuestionBank")
    public WebResult viewQuestionBank(@RequestParam Integer questionBankId, @RequestParam Long page, @RequestParam Long pageSize){
        Page records = questionService.selectBankByPage(questionBankId,page,pageSize);
        return WebResult.ok().data(records);
    }
    
    @GetMapping("/selectBankName")
    public WebResult selectBankName(){
        List<Map<String, Object>> questionBankList =  questionBankService.listMaps(new QueryWrapper<QuestionBank>().lambda().select());
        return WebResult.ok().data(questionBankList);
    }

    @GetMapping("/delQuestion")
    public WebResult delQuestion(@RequestParam Integer id){
        questionMapper.delQuestionInBank(id);
        return WebResult.ok().message("删除成功");
    }
}
