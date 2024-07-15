package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface ExamInfoSummaryService extends IService<ExamInfoSummary> {

    Map<String, Object> viewMain(Integer userId,Integer noChallenge, Long page, Long pageSize);

    Map<String, Object> selectCondition(Integer userId, String startTime, String endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge, Long page, Long pageSize);

    List<ExamRecordDto> exportExamRecord(Integer userId,Integer noChallenge);

    XSSFWorkbook createExcel(List<ExamRecordDto> examRecordDtoList);

    Map<String, Object> viewLearnScore(Long page, Long pageSize);

    List<ExamLearnScore> exportLearnScore();

    XSSFWorkbook createLearnScoreExcel(List<ExamLearnScore> examInfoSummaryList);

    Map<String, Object> selectLearnScore(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);

    Map<String, Object> learnScoreCondition(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);

    Map<String, Object> viewLearnTime(Long page, Long pageSize);

    List<ExamLearnTime> exportLearnTime();

    XSSFWorkbook createLearnTimeExcel(List<ExamLearnTime> examLearnTimeList);

    Map<String, Object> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize);

    Map<String, Object> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize);

    Map<String, Object> viewWrongMain(Integer userId, Long page, Long pageSize);

    Map<String, Object> correctingTestPaper(Integer id, Integer userId, List<String> userAnswer);

    Map<String, Object> selectWrong(Integer userId, Integer type, String stem, Long page, Long pageSize);
}
