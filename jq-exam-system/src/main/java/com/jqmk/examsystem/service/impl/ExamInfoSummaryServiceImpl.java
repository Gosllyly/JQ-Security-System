package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.dto.WrongQuestion;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
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
    private ExamInfoSummaryMapper examInfoSummaryMapper;

    @Override
    public List<ExamRecordDto> viewMain(Integer userId, Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDtos = examInfoSummaryMapper.selectMain(userId,page,pageSize);
        return examRecordDtos;
     }

    @Override
    public List<ExamRecordDto> selectCondition(Integer userId, LocalDateTime startTime, LocalDateTime endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username, Long page, Long pageSize) {
        List<ExamRecordDto> examRecordDtoList = examInfoSummaryMapper.selectCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,page,pageSize);
        return examRecordDtoList;
    }

    @Override
    public List<ExamInfoSummary> exportExamRecord(Integer userId) {
        List<ExamInfoSummary> examInfoSummaryList = examInfoSummaryMapper.selectList(new QueryWrapper<ExamInfoSummary>().select("name","end_time","exam_results","score")
                .eq(userId != null, "user_id", userId));
        return examInfoSummaryList;
    }

    @Override
    public XSSFWorkbook createExcel(List<ExamInfoSummary> records, Integer userId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int rowNum = 0;
        // 创建表头行并设置字段名
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("试卷名称");
        headerRow.createCell(1).setCellValue("完成时间");
        headerRow.createCell(2).setCellValue("考试成绩结果");
        headerRow.createCell(3).setCellValue("分数");

        // 将数据写入 Excel 文件
        for (ExamInfoSummary record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getName());
            row.createCell(1).setCellValue(record.getEndTime());
            row.createCell(2).setCellValue(conventExamResults(record.getExamResults()));
            row.createCell(3).setCellValue(record.getScore());;
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
