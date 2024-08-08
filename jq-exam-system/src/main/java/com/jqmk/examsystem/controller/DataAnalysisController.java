package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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


    /**
     * 当天考试成绩，合格率饼形图
     * @return
     */
    @GetMapping("/resultPie")
    public WebResult ExamResultPie() {
        return WebResult.ok().data(examInfoSummaryMapper.passRatePie());
    }

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
    @GetMapping("/examNum")
    public WebResult examNum() {
        return WebResult.ok().data(examInfoSummaryMapper.takeTestNum());
    }

    /**
     * 7天考试人数，总人数饼形图
     * @return
     */
    @GetMapping("/examNumHistogram")
    public WebResult examNumHistogram() {
        return WebResult.ok().data(examInfoSummaryMapper.takeTestNumHistogram());
    }

    /**
     * 当天风险人数饼形图
     * @return
     */
    @GetMapping("/riskPie")
    public WebResult riskPie() {
        return WebResult.ok().data(examInfoSummaryMapper.riskPie());
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
     * 当天风险人数比例折线图
     * @return
     */
    @GetMapping("/riskHistogram")
    public WebResult riskHistogram() {
        return WebResult.ok().data(examInfoSummaryMapper.riskHistogram());
    }

}
