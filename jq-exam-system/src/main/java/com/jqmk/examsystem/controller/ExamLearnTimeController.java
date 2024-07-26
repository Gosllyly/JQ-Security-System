package com.jqmk.examsystem.controller;

import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.WebResult;
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
 * @ClassName ExamLearnTimeController
 * @Author tian
 * @Date 2024/6/26 8:59
 * @Description 考试学时查询界面接口
 */
@RestController
@RequestMapping("/learningHours")
public class ExamLearnTimeController {

    @Resource
    private ExamInfoSummaryService examInfoSummaryService;

    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam Long page, @RequestParam Long pageSize) {
        return WebResult.ok().data(examInfoSummaryService.viewLearnTime(page,pageSize));
    }

    @GetMapping ("/exportAll")
    public void exportLearnTime(HttpServletResponse response) {
        examInfoSummaryService.exportLearnTime(response);
    }

    /**
     *
     * @param detailDisplay 0代表详细学时，1代表学时汇总
     * @return
     */
    @GetMapping("/select")
    public WebResult selectTestRuler(@RequestParam(required=false) String deptName, @RequestParam(required=false) String username,
                                     @RequestParam(required=false) Integer detailDisplay,@RequestParam(required=false) String name,
                                     @RequestParam Long page, @RequestParam Long pageSize) {
        if (detailDisplay == 0) {
            return WebResult.ok().data(examInfoSummaryService.selectLearnTime(deptName,username,name,page,pageSize));
        }else {
            return WebResult.ok().data(examInfoSummaryService.learnTimeCondition(deptName,username,name,page,pageSize));
        }
    }
}