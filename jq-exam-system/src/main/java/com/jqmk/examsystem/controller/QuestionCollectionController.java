package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.QuestionCollection;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionCollectionService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 收藏题目界面 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/collection")
public class QuestionCollectionController {

    @Resource
    private QuestionCollectionService questionCollectionService;
    @Resource
    private QuestionMapper questionMapper;

    @GetMapping("/main")
    public WebResult viewCollectionMain(Integer userId, Long page, Long pageSize) {
        Page<QuestionCollection> questionPage = new Page<>(page,pageSize);
        Page examCrowdManages =  questionPage.setRecords(questionCollectionService.page(questionPage,new QueryWrapper<QuestionCollection>().eq("user_id",userId)
                .orderByDesc("collection_time")).getRecords());
        return WebResult.ok().data(examCrowdManages);
    }

    @PostMapping("/add")
    public WebResult addCrowdManage(@RequestBody QuestionCollection questionCollection) {
        String stem = questionMapper.selectStem(Math.toIntExact(questionCollection.getQuestionId()));
        if (questionMapper.countCollectionQuestion(stem,questionCollection.getUserId())<1) {
            questionCollectionService.collection(questionCollection);
            return WebResult.ok().message("收藏题目成功");
        }else {
            return WebResult.fail().message("该题目已被收藏，请勿重复收藏");
        }
    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody QuestionCollection questionCollection) {
        questionCollectionService.removeCollection(questionCollection);
        return WebResult.ok().message("删除成功");
    }

    @GetMapping("/select")
    public WebResult selectCollection(Integer userId,Integer type,String stem, Long page, Long pageSize) {
        Page<QuestionCollection> questionPage = new Page<>(page,pageSize);
        Page examCrowdManages =  questionPage.setRecords(questionCollectionService.page(questionPage,new QueryWrapper<QuestionCollection>()
                .eq("user_id",userId).eq(type!=null,"type",type).like(stem!=null,"stem",stem)
                .orderByDesc("collection_time")).getRecords());
        return WebResult.ok().data(examCrowdManages);
    }

    @GetMapping("/getId")
    public WebResult getCollectionId(@RequestParam Integer userId,@RequestParam Integer testId) {
        return WebResult.ok().data(questionMapper.getCollectionId(userId,testId));
    }
}
