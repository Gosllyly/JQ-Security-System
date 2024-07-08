package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.*;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.entity.TestPaperQuestion;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.service.TestPaperQuestionService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/examRecord")
public class ExamInfoSummaryController {

    @Resource
    private ExamInfoSummaryService examInfoSummaryService;
    @Resource
    private ExamInfoSummaryMapper examInfoSummaryMapper;
    @Resource
    private TestPaperQuestionService testPaperQuestionService;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionService questionService;



    /**
     * 考试成绩查询的主界面
     *
     */
    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam(required = false) Integer userId, @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 0;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.viewMain(userId,noChallenge,page,pageSize));
    }

    /**
     * 按条件进行查询
     * @return
     */
    @GetMapping("/select")
    @Transactional
    public WebResult selectCondition(@RequestParam(required = false) Integer userId, @RequestParam(required = false) LocalDateTime startTime, @RequestParam(required = false) LocalDateTime endTime,
                                     @RequestParam(required = false) Integer examCategoryId,@RequestParam(required = false) String name,@RequestParam(required = false) Integer examResults,
                                     @RequestParam(required = false) String deptName,@RequestParam(required = false) String  jobType,@RequestParam(required = false) String username,
                                     @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 0;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.selectCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,noChallenge,page,pageSize));
    }

    /**
     * 同上缺少对答题情况的判断和插入，目前只返回姓名，时间，总分数，结果
     */
    @GetMapping ("/exportAll")
    public WebResult exportQuestionBank(@RequestParam(required = false) Integer userId, HttpServletResponse response) throws IOException {
        Integer noChallenge=0;
        List<ExamRecordDto> examInfoSummaryList = examInfoSummaryService.exportExamRecord(userId,noChallenge);
        // 使用ExportService创建并填充Excel文件
        XSSFWorkbook workbook = examInfoSummaryService.createExcel(examInfoSummaryList);
        // 将Excel文件作为响应发送给客户端
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=records.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
        return WebResult.ok().message("导出成功");
    }

    @GetMapping("/viewExamName")
    public WebResult viewExamName() {
        List<String> examNameList  =  examInfoSummaryService.listObjs(new QueryWrapper<ExamInfoSummary>().lambda().select(ExamInfoSummary::getName).orderByDesc(ExamInfoSummary::getUpdateTime), Object::toString);
        return WebResult.ok().data(examNameList);
    }

    /**
     * 缺少从错题列表里将错题的作答情况取出一并返回的功能
     */
    @GetMapping("/wrongMain")
    public WebResult viewWrongMain(Integer user_id,Long page,Long pageSize) {
        List<WrongQuestion> examNameList  =  examInfoSummaryService.viewWrongMain(user_id,page,pageSize);
        return WebResult.ok().data(examNameList);
    }

    @GetMapping("/correctExam")//这个id是test_paper的id
    public WebResult correctExam(@RequestParam Integer id,@RequestParam Integer userId,@RequestParam List<String> userAnswer) {
        Integer examSummary = examInfoSummaryService.insertNewRecord(userId,id,StringsUtil.stringRecom(userAnswer.toString()));//生成新的问卷方法，用于后续进行成绩录入
        questionMapper.insertRecord(id,examSummary,userId);//插入新的答题情况记录
        Integer testId = questionMapper.selectId(id,userId);//找到刚插入的id
        //通过传入，得到问题id
        List<String> list =  testPaperQuestionService.listObjs(new QueryWrapper<TestPaperQuestion>().lambda().select(TestPaperQuestion::getQuestionId).eq(TestPaperQuestion::getTestPaperId,id), Object::toString);
        JSONObject json = new JSONObject(StringsUtil.stringRecom(userAnswer.toString()));
        String a =StringsUtil.listWipe(list.get(list.size()-1).toString());
        List<String> list1= Arrays.asList(a.split(","));
        System.out.println("json="+json);
        for (int i = 0; i < list1.size(); i++) {
            Object obj = list1.get(i);
            //list1.get(i)为问题id，通过问题的id查找对应的正确答案
            String daan = questionMapper.selectCurrent(Integer.valueOf(list1.get(i)));
            System.out.println("题号" + list1.get(i));
            System.out.println("用户选的" + json.getString(list1.get(i)));
            System.out.println("答案" + StringsUtil.stringRecom(daan).replaceAll("\"", ""));
            if (json.getString(list1.get(i)).equals("")) {
                System.out.println("======未作答=====");
                questionMapper.addNoReply(testId);
            } else if (StringsUtil.stringRecom(daan).replaceAll("\"", "").equals(json.getString(list1.get(i)))) {
                System.out.println("======答对了=====");
                questionService.addCurrent(id,testId,list1.get(i),examSummary);
            } else if (!StringsUtil.stringRecom(daan).replaceAll("\"", "").equals(json.getString(list1.get(i)))) {
                System.out.println("======答错了=====");
                questionService.addWrongs(id,testId,list1.get(i),examSummary);
            }
        }
        //统计完分数，对试卷进行设置结束时间
        examInfoSummaryService.wrapTestPaper(examSummary,id);
        String questionIds = questionMapper.selectQuestionIds(id);
        List<ExamQuestion> question = questionMapper.selectQuestion(StringsUtil.stringWipe(questionIds));
        List<ExamInfoSummaryDto> examInfoSummaryList = questionMapper.selectUserAnswer(examSummary);
        Map<String, Object> res = new HashMap();
        res.put("question", question);
        res.put("answer", examInfoSummaryList);
        return WebResult.ok().data(res);
    }
    @GetMapping("/viewById")
    public WebResult viewExamInfoSummary(Integer id) {
        ExamInfoSummary examInfoSummary = examInfoSummaryMapper.selectById(id);
        String questionIds = questionMapper.selectQuestionIds(examInfoSummary.getTestPaperId());
        List<ExamQuestion> question = questionMapper.selectQuestion(StringsUtil.stringWipe(questionIds));
        List<ExamInfoSummaryDto> examInfoSummaryList = questionMapper.selectUserAnswer(id);
        Map<String, Object> res = new HashMap();
        res.put("question", question);
        res.put("answer", examInfoSummaryList);
        return WebResult.ok().data(res);
    }
}
