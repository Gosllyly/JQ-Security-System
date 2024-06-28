package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.*;
import com.jqmk.examsystem.mapper.ExamCategoryMapper;
import com.jqmk.examsystem.mapper.ExamCrowdManageMapper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.mapper.UserMapper;
import com.jqmk.examsystem.service.TestPaperQuestionService;
import com.jqmk.examsystem.service.TestPaperService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 试卷，及其考试规则愚得分规则 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/exam")
public class TestPaperController {

    @Resource
    private TestPaperService testPaperService;
    @Resource
    private TestPaperMapper testPaperMapper;
    @Resource
    private ExamCategoryMapper examCategoryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ExamCrowdManageMapper examCrowdManageMapper;
    @Resource
    private TestPaperQuestionService testPaperQuestionService;


    @GetMapping("/main")
    @Transactional
    public WebResult viewMain() {
        return WebResult.ok().data(testPaperMapper.selectMaps(new QueryWrapper<TestPaper>()
                .select("id","name","exam_category_id","duration","pass_score","start_time","end_time","status","can_redo","time_limited")
                .eq("no_challenge",0).orderByDesc("update_time")));
    }

    @GetMapping("/examCrowd")
    public WebResult examCrowd() {
        return WebResult.ok().data(examCrowdManageMapper.selectMaps(new QueryWrapper<ExamCrowdManage>().select("id","crowd_name","include_peoples")));
    }

    @PostMapping("/add")
    public WebResult addTestPaperRuler(@RequestBody TestPaper testPaper) {
        testPaper.setNoChallenge(0);
        testPaperService.save(testPaper);
        return WebResult.ok().message("创建考试规则成功");
    }

    @PostMapping("/update")
    public WebResult updateTestPaperRuler(@RequestBody TestPaper testPaper) {
        testPaper.setUpdateTime(LocalDateTime.now());
        testPaperService.updateById(testPaper);
        return WebResult.ok().message("更新成功");
    }

    @GetMapping("/category")
    public WebResult categorySelect() {
        return WebResult.ok().data(examCategoryMapper.selectMaps(new QueryWrapper<ExamCategory>().select("id","category_name","parent_id").eq( "deleted", 0)));
    }

    @GetMapping("/viewUsername")
    public WebResult viewUsername() {
        return WebResult.ok().data(userMapper.selectMaps(new QueryWrapper<User>().lambda().select(User::getUsername).eq( User::getDeleteFlag, 0)));
    }

    @GetMapping("/select")
    public WebResult selectTestRuler(@RequestParam(required=false) Integer examCategoryId,@RequestParam(required=false) String name,@RequestParam(required=false) Integer status) {
        return WebResult.ok().data(testPaperMapper.selectMaps(new QueryWrapper<TestPaper>()
                .select("id","name","exam_category_id","duration","pass_score","start_time","end_time","status","can_redo","time_limited")
                .eq(examCategoryId != null, "exam_category_id", examCategoryId)
                .eq("no_challenge",0)
                .eq(name != null, "name", name)
                .eq(status != null, "status", status).orderByDesc("update_time")));
    }

    /**
     * 我的考试界面
     * @param userId
     * @return
     */
    @GetMapping("/myExam")
    public WebResult selectMyExam(@RequestParam Integer userId) {
        User user =  userMapper.selectById(userId);
        return WebResult.ok().data(testPaperMapper.selectMaps(new QueryWrapper<TestPaper>()
                .select("id","name","duration","pass_score","start_time","end_time","single_choice_num",
                        "single_choice_score","multi_choice_num","multi_choice_score","judge_choice_num","judge_choice_score")
                .eq( "status", 0)
                .eq("no_challenge",0)
                .like("exam_crowd_ids",user.getUsername())));
    }

    /**
     * 通过传入的试卷规则id，生成对应的试卷，并把试卷问题的详情返回前端
     * @param id
     * @return
     */
    @GetMapping("/startExam")
    public WebResult startMyExam(@RequestParam Integer id) {
        testPaperQuestionService.startExam(id);
        return WebResult.ok().data(testPaperQuestionService.startExam(id));
    }

    /** （未完成）
     * 接收返回的试卷答题情况，判断分数
     * @param examInfoSummary
     * @return
     */
    @PostMapping("/correctExam")
    public WebResult correctExam(@RequestBody ExamInfoSummary examInfoSummary) {

        return WebResult.ok();
    }
}
