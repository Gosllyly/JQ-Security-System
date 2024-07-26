package com.jqmk.examsystem.controller;

import com.jqmk.examsystem.dto.ExamLearnScore;
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
 * @ClassName ExamLearnScoreController
 * @Author tian
 * @Date 2024/6/25 15:47
 * @Description 考试学分查询界面接口
 */
@RestController
@RequestMapping("/credit")
public class ExamLearnScoreController {

    @Resource
    private ExamInfoSummaryService examInfoSummaryService;

    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam Long page, @RequestParam Long pageSize) {
        return WebResult.ok().data(examInfoSummaryService.viewLearnScore(page,pageSize));
    }

    @GetMapping ("/exportAll")
    public void exportLearnScore(HttpServletResponse response){
        examInfoSummaryService.exportLearnScore(response);
        // 使用ExportService创建并填充Excel文件
    }

    /**
     *
     * @param detailDisplay 0代表详细学分，1代表学分汇总
     * @return
     */
    @GetMapping("/select")
    public WebResult selectTestRuler(@RequestParam(required=false) String deptName, @RequestParam(required=false) Integer examCategoryId,
                                     @RequestParam(required=false) Integer detailDisplay, @RequestParam(required=false) String username,
                                     @RequestParam(required=false) String name, @RequestParam(required=false) String startTime,
                                     @RequestParam(required=false) String endTime,@RequestParam Long page, @RequestParam Long pageSize) {
        if (detailDisplay == 0) {
            return WebResult.ok().data(examInfoSummaryService.selectLearnScore(deptName,examCategoryId,username,name,startTime,endTime,page,pageSize));
        }else {
            return WebResult.ok().data(examInfoSummaryService.learnScoreCondition(deptName,examCategoryId,username,name,startTime,endTime,page,pageSize));
        }
    }
}
