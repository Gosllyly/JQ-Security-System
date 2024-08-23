package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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

    Map<String, Object> selectCondition(Integer userId, String startTime, String endTime, String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge, Long page, Long pageSize);

    void exportExamRecord(Integer userId, Integer noChallenge, HttpServletResponse response);


    Map<String, Object> viewLearnScore(Long page, Long pageSize);

    void exportLearnScore(HttpServletResponse response);

    Map<String, Object> selectLearnScore(String deptName, String username, String name, String startTime, String endTime, Long page, Long pageSize);

    Map<String, Object> learnScoreCondition(String deptName, String username, String name, String startTime, String endTime, Long page, Long pageSize);

    Map<String, Object> viewLearnTime(Long page, Long pageSize);

    void exportLearnTime(HttpServletResponse response);


    Map<String, Object> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize);

    Map<String, Object> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize);

    Map<String, Object> viewWrongMain(Integer userId, Long page, Long pageSize);

    Map<String, Object> correctingTestPaper(Integer id, Integer userId, List<String> userAnswer);

    Map<String, Object> selectWrong(Integer userId, Integer type, String stem, Long page, Long pageSize);

    Map<String, Object> examDetail(Integer id);

    Map<String, Object> examPeoplePie(@RequestParam Integer testId);

    List<Map<String, Object>> examPercentage(String time,Integer id,Integer size);

    List<Map<String, Object>> examHistogram(String time, Integer id, Integer size);

    Map<String, Object> title();
}
