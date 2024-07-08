package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.dto.WrongQuestion;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> viewMain(Integer userId,Integer noChallenge, Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDto = examInfoSummaryMapper.selectMain(userId,noChallenge,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.countMain(userId,noChallenge);
        Map<String, Object> res = new HashMap();
        res.put("data", examRecordDto);
        res.put("total", total);
        return res;
     }

    @Override
    public Map<String, Object> selectCondition(Integer userId, LocalDateTime startTime, LocalDateTime endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username, Integer noChallenge,Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDtoList = examInfoSummaryMapper.selectCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,noChallenge,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.countCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,noChallenge);
        Map<String, Object> res = new HashMap();
        res.put("data", examRecordDtoList);
        res.put("total", total);
        return res;
    }

    @Override
    public List<ExamRecordDto> exportExamRecord(Integer userId,Integer noChallenge) {
        List<ExamRecordDto> examRecordDtoList = examInfoSummaryMapper.exportAll(userId,noChallenge);
        return examRecordDtoList;
    }

    @Override
    public XSSFWorkbook createExcel(List<ExamRecordDto> records) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int rowNum = 0;
        // 创建表头行并设置字段名
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("试卷名称");
        headerRow.createCell(1).setCellValue("员工姓名");
        headerRow.createCell(2).setCellValue("部门");
        headerRow.createCell(3).setCellValue("工种");
        headerRow.createCell(4).setCellValue("考试时间");
        headerRow.createCell(5).setCellValue("考试用时");
        headerRow.createCell(6).setCellValue("答题总数");
        headerRow.createCell(7).setCellValue("正确数");
        headerRow.createCell(8).setCellValue("错误数");
        headerRow.createCell(9).setCellValue("未做数");
        headerRow.createCell(10).setCellValue("总分");
        headerRow.createCell(11).setCellValue("成绩");
        headerRow.createCell(12).setCellValue("考试情况");


        // 将数据写入 Excel 文件
        for (ExamRecordDto record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getName());
            row.createCell(1).setCellValue(record.getUsername());
            row.createCell(2).setCellValue(record.getDeptName());
            row.createCell(3).setCellValue(record.getJobType());
            row.createCell(4).setCellValue(record.getStartTime());
            row.createCell(5).setCellValue(record.getUnavailable());
            row.createCell(6).setCellValue(record.getAnswerCount());
            row.createCell(7).setCellValue(record.getAnswerCorrect());
            row.createCell(8).setCellValue(record.getAnswerWrong());
            row.createCell(9).setCellValue(record.getNoReply());
            row.createCell(10).setCellValue(100);
            row.createCell(11).setCellValue(record.getScore());
            row.createCell(12).setCellValue(conventExamResults(record.getExamResults()));
        }

        return workbook;
    }

    @Override
    public Map<String, Object> viewLearnScore(Long page, Long pageSize) {
        List<ExamLearnScore>  viewInterface = examInfoSummaryMapper.viewInterface((page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.viewInterfaceCount();
        Map<String, Object> res = new HashMap();
        res.put("data", viewInterface);
        res.put("total", total);
        return res;
    }

    @Override
    public List<ExamLearnScore> exportLearnScore() {
        List<ExamLearnScore> examInfoSummaryList = examInfoSummaryMapper.viewInterface(0L,Long.MAX_VALUE);
        return examInfoSummaryList;
    }

    @Override
    public XSSFWorkbook createLearnScoreExcel(List<ExamLearnScore> records) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int rowNum = 0;
        // 创建表头行并设置字段名
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("员工姓名");
        headerRow.createCell(1).setCellValue("部门名称");
        headerRow.createCell(2).setCellValue("获取途径");
        headerRow.createCell(3).setCellValue("获取分数");
        headerRow.createCell(4).setCellValue("获取时间");

        // 将数据写入 Excel 文件
        for (ExamLearnScore record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getName());
            row.createCell(1).setCellValue(record.getDeptName());
            row.createCell(2).setCellValue(record.getName());
            row.createCell(3).setCellValue(record.getObtainLearningScore());
            row.createCell(4).setCellValue(record.getEndTime());
        }

        return workbook;
    }

    @Override
    public Map<String, Object> selectLearnScore(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize) {
        List<ExamLearnScore>  examLearnScoreList = examInfoSummaryMapper.selectLearnScore(deptName,examCategoryId,username,name,startTime,endTime,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.selectLearnScoreCount(deptName,examCategoryId,username,name,startTime,endTime);
        Map<String, Object> res = new HashMap();
        res.put("data", examLearnScoreList);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> learnScoreCondition(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize) {
        List<ExamLearnScore>  learnScoreCondition = examInfoSummaryMapper.learnScoreCondition(deptName,examCategoryId,username,name,startTime,endTime,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.learnScoreConditionCount(deptName,examCategoryId,username,name,startTime,endTime);
        Map<String, Object> res = new HashMap();
        res.put("data", learnScoreCondition);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> viewLearnTime(Long page, Long pageSize) {
        List<ExamLearnTime>  viewInterface = examInfoSummaryMapper.learnTimeInterface((page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.learnTimeInterfaceCount();
        Map<String, Object> res = new HashMap();
        res.put("data", viewInterface);
        res.put("total", total);
        return res;
    }

    @Override
    public List<ExamLearnTime> exportLearnTime() {
        List<ExamLearnTime> examLearnTimes = examInfoSummaryMapper.learnTimeInterface(0L,Long.MAX_VALUE);
        return examLearnTimes;
    }

    @Override
    public XSSFWorkbook createLearnTimeExcel(List<ExamLearnTime> records) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int rowNum = 0;
        // 创建表头行并设置字段名
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("员工姓名");
        headerRow.createCell(1).setCellValue("部门名称");
        headerRow.createCell(2).setCellValue("获取途径");
        headerRow.createCell(3).setCellValue("可获取学时");
        headerRow.createCell(4).setCellValue("实际获取学时");
        headerRow.createCell(5).setCellValue("获取时间");

        // 将数据写入 Excel 文件
        for (ExamLearnTime record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getName());
            row.createCell(1).setCellValue(record.getDeptName());
            row.createCell(2).setCellValue(record.getName());
            row.createCell(3).setCellValue(record.getLearningTime());
            row.createCell(4).setCellValue(record.getObtainLearningTime());
            row.createCell(5).setCellValue(record.getEndTime());
        }

        return workbook;
    }

    @Override
    public Map<String, Object> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize) {
        List<ExamLearnTime>  examLearnTimes = examInfoSummaryMapper.selectLearnTime(deptName,username,name,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.selectLearnTimeCount(deptName,username,name);
        Map<String, Object> res = new HashMap();
        res.put("data", examLearnTimes);
        res.put("total", total);
        return res;
    }

    @Override
    public Map<String, Object> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize) {
        List<ExamLearnTime>  learnTimeCondition = examInfoSummaryMapper.learnTimeCondition(deptName,username,name,(page - 1) * pageSize,pageSize);
        Integer total = examInfoSummaryMapper.learnTimeConditionCount(deptName,username,name);
        Map<String, Object> res = new HashMap();
        res.put("data", learnTimeCondition);
        res.put("total", total);
        return res;
    }

    @Override
    public List<WrongQuestion> viewWrongMain(Integer user_id, Long page, Long pageSize) {
        return null;
    }

    @Override
    public Integer insertNewRecord(Integer userId, Integer id, String userAnswer) {
        //传入了问卷的id和用户id，用户答案列表
        examInfoSummaryMapper.insertNewRecord(userId,id);
        Integer newId = examInfoSummaryMapper.selectId(userId,id);
        examInfoSummaryMapper.updateUserAnswer(newId,userAnswer);
        return newId;
    }

    @Override
    public void wrapTestPaper(Integer examSummary,Integer id) {//id是问卷规则的id
        Integer examResults = examInfoSummaryMapper.equalsScore(examSummary);//判断是不是及格
        if (examResults==1) {//及格
            Integer learningScore = testPaperMapper.selectLearningScore(id);
            Integer learningTime = testPaperMapper.selectLearningTime(id);
            examInfoSummaryMapper.updateTestPaper(examSummary,learningScore,learningTime,examResults);
        }else if (examResults==2) {//不及格
            Integer learningScore = 0;
            Integer learningTime = 0;
            examInfoSummaryMapper.updateTestPaper(examSummary,learningScore,learningTime,examResults);
        }
    }

    public String conventExamResults(Integer examResults) {
        String results = null;
        //1代表及格，2不及格，0未参考
        if (examResults.equals(1)) {
            results="及格";
        }else if (examResults.equals(2)) {
            results="不及格";
        }else {
            results="未参考";
        }
        return results;
    }
}
