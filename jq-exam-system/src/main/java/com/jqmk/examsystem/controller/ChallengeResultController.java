package com.jqmk.examsystem.controller;

import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.service.ExamCategoryService;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ChallengeResultController
 * @Author tian
 * @Date 2024/6/25 15:25
 * @Description 挑战答题结果界面接口
 */
@RestController
@RequestMapping("/challengeResult")
public class ChallengeResultController {

    @Resource
    private ExamInfoSummaryService examInfoSummaryService;
    @Resource
    private ExamCategoryService examCategoryService;


    /**
     * 挑战成绩查询的主界面，缺少对user_answers的遍历，没有反馈答题总数，错题，正确数的字段
     *
     */
    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam(required = false) Integer userId, @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 1;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.viewMain(userId,noChallenge,page,pageSize));
    }
    /**
     * 同上接口一样，缺少答题数目和分数的返回
     * @return
     */
    @GetMapping("/select")
    @Transactional
    public WebResult selectCondition(@RequestParam(required = false) Integer userId, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) String name, @RequestParam(required = false) Integer examResults,
                                     @RequestParam(required = false) String deptName, @RequestParam(required = false) String  jobType, @RequestParam(required = false) String username,
                                     @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 1;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.selectCondition(userId,startTime,endTime,name,examResults,deptName,jobType,username,noChallenge,page,pageSize));
    }

    /**
     * 同上缺少对答题情况的判断和插入，目前只返回姓名，时间，总分数，结果
     */
    @GetMapping ("/exportAll")
    public void exportQuestionBank(@RequestParam(required = false) Integer userId, HttpServletResponse response) throws IOException {
        Integer noChallenge = 1;
        examInfoSummaryService.exportExamRecord(userId,noChallenge,response);
    }

}
