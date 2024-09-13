package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.UserProfileMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import com.jqmk.examsystem.service.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * <p>
 * 数据洞察界面 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/analysis")
public class DataAnalysisController {

    @Resource
    ExamInfoSummaryMapper examInfoSummaryMapper;

    @Resource
    ExamInfoSummaryService examInfoSummaryService;
    @Resource
    UserProfileService userProfileService;
    @Resource
    UserProfileMapper userProfileMapper;


    /**
     * 当天考试成绩，合格率饼形图
     * @return
     */
//    @GetMapping("/resultPie")
//    public WebResult ExamResultPie() {
//        return WebResult.ok().data(examInfoSummaryMapper.passRatePie());
//    }

    /**
     * 7天考试成绩，合格率柱状图
     * @return
     */
    @GetMapping("/resultHistogram")
    public WebResult ExamResultPieSeven() {
        return WebResult.ok().data(examInfoSummaryMapper.passRateHistogram());
    }

    /**
     * 当天考试人数，总人数饼形图
     * @return
     */
//    @GetMapping("/examNum")
//    public WebResult examNum() {
//        return WebResult.ok().data(examInfoSummaryMapper.takeTestNum());
//    }

    /**
     * 7天考试人数，总人数饼形图
     * @return
     */
    @GetMapping("/examNumHistogram")
    public WebResult examNumHistogram() {
        return WebResult.ok().data(examInfoSummaryMapper.takeTestNumHistogram());
    }

    /**
     * 7天风险人数比例折线图
     * @return
     */
    @GetMapping("/riskPercentage")
    public WebResult riskPercentage() {
        return WebResult.ok().data(examInfoSummaryMapper.riskPercentage());
    }


    /**
     * 下面为数据洞察二期接口
     */

    /**
     * 当天风险人数饼形图,以及可选择日期饼状图
     * @return
     */
    @GetMapping("/riskPie")
    public WebResult riskPie(@RequestParam String time) {
        //return WebResult.ok().data(examInfoSummaryMapper.riskPie());
        return WebResult.ok().data(userProfileMapper.riskPieOtherDay(time));
    }

    @GetMapping("/riskPercent")
    public WebResult riskPercent(@RequestParam String startTime,@RequestParam Integer type) {
        return WebResult.ok().data(userProfileService.riskPercentage(startTime,type));
    }

    @GetMapping("/riskHistogram")
    public WebResult riskHistogram(@RequestParam String time) {
        return WebResult.ok().data(userProfileMapper.riskHistogram(time));
    }

    @GetMapping("/violationData")
    public WebResult violationData(@RequestParam(required = false) String time,@RequestParam(required = false) String deptName) {
        return WebResult.ok().data(userProfileMapper.violationData(time,deptName));
    }

    @GetMapping("/violationPie")
    public WebResult violationPie(@RequestParam(required = false) String time) {
        return WebResult.ok().data(userProfileMapper.violationPie(time));
    }
    @GetMapping("/examPeoplePie")
    public WebResult examPeoplePie(@RequestParam Integer testId) {
        return WebResult.ok().data(examInfoSummaryService.examPeoplePie(testId));
    }

    @GetMapping("/examPercentage")
    public WebResult examPercentage(@RequestParam(required = false) String time,@RequestParam(required = false) Integer id,@RequestParam Integer size) {
        return WebResult.ok().data(examInfoSummaryService.examPercentage(time,id,size));
    }

    @GetMapping("/examHistogram")
    public WebResult examHistogram(@RequestParam(required = false) String time,@RequestParam(required = false) Integer id,@RequestParam Integer size) {
        return WebResult.ok().data(examInfoSummaryService.examHistogram(time,id,size));
    }

    @GetMapping("/scoreHistogram")
    public WebResult scoreHistogram(@RequestParam Integer size,@RequestParam String ids){
        return WebResult.ok().data(examInfoSummaryMapper.scoreHistogram(size,ids));
    }

    @GetMapping("/title")
    public WebResult title() {
        return WebResult.ok().data(examInfoSummaryService.title());
    }

    @GetMapping("/annular")
    public WebResult annularPie(@RequestParam(required = false) String deptName,@RequestParam Integer size) {
        return WebResult.ok().data(examInfoSummaryService.annularPie(deptName,size));
    }

    @GetMapping("/numRank")
    public WebResult numRank(@RequestParam String starTime,@RequestParam String endTime) {
        return WebResult.ok().data(examInfoSummaryMapper.numRank(starTime,endTime));
    }

    @GetMapping("/testId")
    public WebResult getTestId() {
        return WebResult.ok().data(examInfoSummaryMapper.getTestId());
    }

    @GetMapping("/passRate")
    public WebResult passRate() {
        return WebResult.ok().data(examInfoSummaryMapper.passRate());
    }

    @GetMapping("/noPassList")
    public WebResult noPassList() {
        return WebResult.ok().data(examInfoSummaryMapper.noPassList());
    }

    @GetMapping("/scoreRate")
    public WebResult scoreRate(@RequestParam(required = false) String starTime,@RequestParam(required = false) String endTime) {
        return WebResult.ok().data(examInfoSummaryMapper.scoreRate(starTime,endTime));
    }

    @GetMapping("/testName")
    public WebResult testName() {
        return WebResult.ok().data(examInfoSummaryMapper.getTestName());
    }
}
