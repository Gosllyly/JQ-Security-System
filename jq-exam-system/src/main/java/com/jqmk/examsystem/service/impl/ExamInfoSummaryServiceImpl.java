package com.jqmk.examsystem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.*;
import com.jqmk.examsystem.dto.export.ExportExamRecord;
import com.jqmk.examsystem.dto.export.ExportQuestion;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.entity.TestPaperQuestion;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.service.TestPaperQuestionService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class ExamInfoSummaryServiceImpl extends ServiceImpl<ExamInfoSummaryMapper, ExamInfoSummary> implements ExamInfoSummaryService {

    @Resource
    private TestPaperMapper testPaperMapper;
    @Resource
    private ExamInfoSummaryMapper examInfoSummaryMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionService questionService;
    @Resource
    private TestPaperQuestionService testPaperQuestionService;

    @Override
    public Map<String, Object> viewMain(Integer userId, Integer noChallenge, Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDto = examInfoSummaryMapper.selectMain(userId, noChallenge, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.countMain(userId, noChallenge);
        Map<String, Object> res = new HashMap();
        res.put("data", examRecordDto);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> selectCondition(Integer userId, String startTime, String endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username, Integer noChallenge, Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDtoList = examInfoSummaryMapper.selectCondition(userId, startTime, endTime, examCategoryId, name, examResults, deptName, jobType, username, noChallenge, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.countCondition(userId, startTime, endTime, examCategoryId, name, examResults, deptName, jobType, username, noChallenge);
        Map<String, Object> res = new HashMap();
        res.put("data", examRecordDtoList);
        res.put("total", total);
        return res;
    }

    @Override
    public void exportExamRecord(Integer userId, Integer noChallenge, HttpServletResponse response) {
        List<ExportExamRecord> examRecordDtoList = examInfoSummaryMapper.exportAll(userId, noChallenge);
        try {
            this.setExcelRecord(response);
            EasyExcel.write(response.getOutputStream())
                    .head(ExportExamRecord.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("考试成绩")
                    .doWrite(examRecordDtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setExcelRecord(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment; filename=record.xlsx");
    }

    @Override
    public Map<String, Object> viewLearnScore(Long page, Long pageSize) {
        List<ExamLearnScore> viewInterface = examInfoSummaryMapper.viewInterface((page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.viewInterfaceCount();
        Map<String, Object> res = new HashMap();
        res.put("data", viewInterface);
        res.put("total", total);
        return res;
    }

    @Override
    public void exportLearnScore(HttpServletResponse response) {
        List<ExamLearnScore> examLearnScoreList = examInfoSummaryMapper.viewInterface(0L, Long.MAX_VALUE);
        try {
            this.setExcelScore(response);
            EasyExcel.write(response.getOutputStream())
                    .head(ExamLearnScore.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("考试学分")
                    .doWrite(examLearnScoreList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setExcelScore(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment; filename=score.xlsx");
    }

    @Override
    public Map<String, Object> selectLearnScore(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize) {
        List<ExamLearnScore> examLearnScoreList = examInfoSummaryMapper.selectLearnScore(deptName, examCategoryId, username, name, startTime, endTime, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.selectLearnScoreCount(deptName, examCategoryId, username, name, startTime, endTime);
        Map<String, Object> res = new HashMap();
        res.put("data", examLearnScoreList);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> learnScoreCondition(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize) {
        List<ExamLearnScore> learnScoreCondition = examInfoSummaryMapper.learnScoreCondition(deptName, examCategoryId, username, name, startTime, endTime, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.learnScoreConditionCount(deptName, examCategoryId, username, name, startTime, endTime);
        Map<String, Object> res = new HashMap();
        res.put("data", learnScoreCondition);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> viewLearnTime(Long page, Long pageSize) {
        List<ExamLearnTime> viewInterface = examInfoSummaryMapper.learnTimeInterface((page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.learnTimeInterfaceCount();
        Map<String, Object> res = new HashMap();
        res.put("data", viewInterface);
        res.put("total", total);
        return res;
    }

    @Override
    public void exportLearnTime(HttpServletResponse response) {
        List<ExamLearnTime> examLearnTimes = examInfoSummaryMapper.learnTimeInterface(0L, Long.MAX_VALUE);
        try {
            this.setExcelTime(response);
            EasyExcel.write(response.getOutputStream())
                    .head(ExamLearnTime.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("考试学时")
                    .doWrite(examLearnTimes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setExcelTime(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment; filename=time.xlsx");
    }

    @Override
    public Map<String, Object> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize) {
        List<ExamLearnTime> examLearnTimes = examInfoSummaryMapper.selectLearnTime(deptName, username, name, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.selectLearnTimeCount(deptName, username, name);
        Map<String, Object> res = new HashMap();
        res.put("data", examLearnTimes);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize) {
        List<ExamLearnTime> learnTimeCondition = examInfoSummaryMapper.learnTimeCondition(deptName, username, name, (page - 1) * pageSize, pageSize);
        Integer total = examInfoSummaryMapper.learnTimeConditionCount(deptName, username, name);
        Map<String, Object> res = new HashMap();
        res.put("data", learnTimeCondition);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> viewWrongMain(Integer userId, Long page, Long pageSize) {
        List<WrongQuestion> errorQuestion = questionMapper.selectErrorQuestion(userId, (page - 1) * pageSize, pageSize);
        Integer total = questionMapper.countErrorQuestion(userId);
        ;
        Map<String, Object> res = new HashMap();
        res.put("question", errorQuestion);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> selectWrong(Integer userId, Integer type, String stem, Long page, Long pageSize) {
        List<WrongQuestion> errorQuestion = questionMapper.errorQuestionCondition(userId, type, stem, (page - 1) * pageSize, pageSize);
        Integer total = questionMapper.countErrorQuestionCondition(userId, type, stem);
        Map<String, Object> res = new HashMap();
        res.put("question", errorQuestion);
        res.put("total", total);
        return res;
    }

    public Integer insertNewRecord(Integer userId, Integer id, String userAnswer) {
        //传入了问卷的id和用户id，用户答案列表
        Integer newId = examInfoSummaryMapper.selectId(userId, id);
        examInfoSummaryMapper.updateUserAnswer(newId, userAnswer);
        return newId;
    }

    public void wrapTestPaper(Integer examSummary, Integer id) {//id是问卷规则的id
        Integer examResults = examInfoSummaryMapper.equalsScore(examSummary);//判断是不是及格
        if (examResults == 1) {//及格
            Integer learningScore = testPaperMapper.selectLearningScore(id);
            Integer learningTime = testPaperMapper.selectLearningTime(id);
            examInfoSummaryMapper.updateTestPaper(examSummary, learningScore, learningTime, examResults);
        } else if (examResults == 2) {//不及格
            Integer learningScore = 0;
            Integer learningTime = 0;
            examInfoSummaryMapper.updateTestPaper(examSummary, learningScore, learningTime, examResults);
        }
    }

    @Override
    public Map<String, Object> correctingTestPaper(Integer id, Integer userId, List<String> userAnswer) {
        Integer examSummary = insertNewRecord(userId, id, StringsUtil.stringRecom(userAnswer.toString()));//生成新的问卷方法，用于后续进行成绩录入
        questionMapper.insertRecord(id, examSummary, userId);//插入新的答题情况记录
        Integer testId = questionMapper.selectId(id, userId);//找到刚插入的id
        //通过传入，得到问题id
        List<String> list = testPaperQuestionService.listObjs(new QueryWrapper<TestPaperQuestion>().lambda().select(TestPaperQuestion::getQuestionId).eq(TestPaperQuestion::getTestPaperId, id), Object::toString);
        JSONObject json = new JSONObject(StringsUtil.stringRecom(userAnswer.toString()));
        String a = StringsUtil.listWipe(list.get(list.size() - 1).toString());
        List<String> list1 = Arrays.asList(a.split(","));
        for (int i = 0; i < list1.size(); i++) {
            //list1.get(i)为问题id，通过问题的id查找对应的正确答案
            String daan = questionMapper.selectCurrent(Integer.valueOf(list1.get(i)));
            if (json.getString(list1.get(i)).equals("")) {
                questionMapper.addNoReply(testId);
            } else if (StringsUtil.stringRecom(daan).replaceAll("\"", "").equals(json.getString(list1.get(i)))) {
                questionService.addCurrent(id, testId, list1.get(i), examSummary);
            } else if (!StringsUtil.stringRecom(daan).replaceAll("\"", "").equals(json.getString(list1.get(i)))) {
                questionService.addWrongs(id, testId, list1.get(i), examSummary, json.getString(list1.get(i)), userId);
            }
        }
        //统计完分数，对试卷进行设置结束时间
        wrapTestPaper(examSummary, id);
        String questionIds = questionMapper.selectQuestionIds(id);
        List<ExamQuestion> question = questionMapper.selectQuestion(StringsUtil.stringWipe(questionIds));
        List<ExamInfoSummaryDto> examInfoSummaryList = questionMapper.selectUserAnswer(examSummary);
        Map<String, Object> res = new HashMap();
        res.put("question", question);
        res.put("answer", examInfoSummaryList);
        return res;
    }
}
