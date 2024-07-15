package com.jqmk.examsystem.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.dto.TestPaperDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.*;
import com.jqmk.examsystem.mapper.*;
import com.jqmk.examsystem.service.ExamCrowdManageService;
import com.jqmk.examsystem.service.TestPaperQuestionService;
import com.jqmk.examsystem.service.TestPaperService;
import com.jqmk.examsystem.service.UserService;
import com.jqmk.examsystem.service.impl.QuestionServiceImpl;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
    private ExamInfoSummaryMapper examInfoSummaryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private ExamCrowdManageMapper examCrowdManageMapper;
    @Resource
    private ExamCrowdManageService examCrowdManageService;
    @Resource
    private TestPaperQuestionService testPaperQuestionService;
    @Resource
    private QuestionServiceImpl questionService;


    @GetMapping("/main")
    @Transactional
    public WebResult viewMain() {
        List<TestPaper> testPaperList = testPaperService.list(new QueryWrapper<TestPaper>()
                .select("id","name","exam_category_id","duration","pass_score","start_time","end_time","status","can_redo","time_limited")
                .eq("no_challenge",0).orderByDesc("update_time"));
        return WebResult.ok().data(BeanUtil.copyToList(testPaperList, TestPaperDto.class));
    }

    @GetMapping("/examCrowd")
    public WebResult examCrowd() {
        return WebResult.ok().data(examCrowdManageMapper.selectMaps(new QueryWrapper<ExamCrowdManage>().select("id","crowd_name","include_peoples")));
    }

    @PostMapping("/add")
    public WebResult addTestPaperRuler(@RequestBody TestPaper testPaper) {
        //判断各个题型数量设置是否合法
        if(!questionService.ifQuestionCountIsLegal(testPaper)){
            return WebResult.fail().message("错误信息！");
        }
        // 验证单选题、多选题和判断题的分数总和是否等于 100 分
        int totalScore = testPaper.getSingleChoiceScore() * testPaper.getSingleChoiceNum()
                + testPaper.getMultiChoiceScore() * testPaper.getMultiChoiceNum()
                + testPaper.getJudgeChoiceScore() * testPaper.getJudgeChoiceNum();
        if (totalScore != 100) {
            return WebResult.fail().message("创建失败！分值总和应为100");
        }

        testPaper.setNoChallenge(0);

        List<String> names = examCrowdManageService.listObjs(new QueryWrapper<ExamCrowdManage>().lambda().select(ExamCrowdManage::getIncludePeoples)
                .like(ExamCrowdManage::getId,testPaper.getExamCategoryId()), Object::toString);
        //从人群中查出人员姓名并拼接
        testPaper.setExamCrowdIds(StringsUtil.stringRecom(names.toString()));
        testPaperService.save(testPaper);
        return WebResult.ok().message("创建考试规则成功");
    }

    @PostMapping("/update")
    public WebResult updateTestPaperRuler(@RequestBody TestPaper testPaper) {
        //判断各个题型数量设置是否合法
        if(!questionService.ifQuestionCountIsLegal(testPaper)){
            return WebResult.fail().message("错误信息！");
        }
        // 验证单选题、多选题和判断题的分数总和是否等于 100 分
        int totalScore = testPaper.getSingleChoiceScore() * testPaper.getSingleChoiceNum()
                + testPaper.getMultiChoiceScore() * testPaper.getMultiChoiceNum()
                + testPaper.getJudgeChoiceScore() * testPaper.getJudgeChoiceNum();
        if (totalScore != 100) {
            return WebResult.fail().message("更新失败！分值总和应为100");
        }

        List<String> names = examCrowdManageService.listObjs(new QueryWrapper<ExamCrowdManage>().lambda().select(ExamCrowdManage::getIncludePeoples)
                .like(ExamCrowdManage::getId,testPaper.getExamCategoryId()), Object::toString);
        testPaper.setUpdateTime(LocalDateTime.now());
        testPaper.setExamCrowdIds(StringsUtil.stringRecom(names.toString()));
        testPaperService.updateById(testPaper);
        return WebResult.ok().message("更新成功");
    }

    @GetMapping("/category")
    public WebResult categorySelect() {
        return WebResult.ok().data(examCategoryMapper.selectMaps(new QueryWrapper<ExamCategory>().select("id","category_name","parent_id").eq( "deleted", 0)));
    }

    @GetMapping("/viewUsername")
    public WebResult viewUsername() {
        List<String> usernameList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getUsername).eq( User::getDeleteFlag, 0), Object::toString);
        Stream<String> distinct = usernameList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    @GetMapping("/select")
    public WebResult selectTestRuler(@RequestParam(required=false) Integer examCategoryId,@RequestParam(required=false) String name,@RequestParam(required=false) Integer status) {
        List<TestPaper> testPaperList = testPaperMapper.selectList(new QueryWrapper<TestPaper>()
                .select("id","name","exam_category_id","duration","pass_score","start_time","end_time","status","can_redo","time_limited")
                .eq(examCategoryId != null, "exam_category_id", examCategoryId)
                .eq("no_challenge",0)
                .eq(name != null, "name", name)
                .eq(status != null, "status", status).orderByDesc("update_time"));
        return WebResult.ok().data(BeanUtil.copyToList(testPaperList, TestPaperDto.class));
    }
    @GetMapping("/selectPeoples")
    public WebResult selectPeoples(Integer id) {
        ExamCrowdManage examCrowdManage = examCrowdManageMapper.selectById(id);
        return WebResult.ok().data(examCrowdManage.getIncludePeoples());
    }
    @GetMapping("/viewExam")
    public WebResult viewExamById(Integer id) {
        TestPaper testPaper = testPaperMapper.selectById(id);
        return WebResult.ok().data(testPaper);
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
    public WebResult startMyExam(@RequestParam Integer id,@RequestParam Integer userId) {
        List<ExamQuestion> examQuestions = testPaperQuestionService.startExam(id,userId);
        if (examQuestions!=null) {
            examInfoSummaryMapper.insertNewRecord(userId,id);
            return WebResult.ok().data(examQuestions);
        }else {
            return WebResult.ok().message("已经达到了答题次数上限");
        }
    }

}
