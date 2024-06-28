package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.dto.WrongQuestion;
import com.jqmk.examsystem.entity.ExamInfoSummary;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/examRecord")
public class ExamInfoSummaryController {

    @Resource
    private ExamInfoSummaryService examInfoSummaryService;
    @Resource
    private ExamCategoryService examCategoryService;


    /**
     * 考试成绩查询的主界面，缺少对user_answers的遍历，没有反馈答题总数，错题，正确数的字段
     *
     */
    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam(required = false) Integer userId, @RequestParam Long page, @RequestParam Long pageSize) {
        examInfoSummaryService.viewMain(userId,page,pageSize);
        return WebResult.ok().data(examInfoSummaryService.viewMain(userId,page,pageSize));
    }

    /**
     * 同上接口一样，缺少答题数目和分数的返回
     * @return
     */
    @GetMapping("/select")
    @Transactional
    public WebResult selectCondition(@RequestParam(required = false) Integer userId, @RequestParam(required = false) LocalDateTime startTime, @RequestParam(required = false) LocalDateTime endTime,
                                     @RequestParam(required = false) Integer examCategoryId,@RequestParam(required = false) String name,@RequestParam(required = false) Integer examResults,
                                     @RequestParam(required = false) String deptName,@RequestParam(required = false) String  jobType,@RequestParam(required = false) String username,
                                     @RequestParam Long page, @RequestParam Long pageSize) {
        examInfoSummaryService.selectCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,page,pageSize);
        return WebResult.ok().data(examInfoSummaryService.viewMain(userId,page,pageSize));
    }

    /**
     * 同上缺少对答题情况的判断和插入，目前只返回姓名，时间，总分数，结果
     */
    @GetMapping ("/exportAll")
    public WebResult exportQuestionBank(@RequestParam(required = false) Integer userId, HttpServletResponse response) throws IOException {
        List<ExamInfoSummary> examInfoSummaryList = examInfoSummaryService.exportExamRecord(userId);
        // 使用ExportService创建并填充Excel文件
        XSSFWorkbook workbook = examInfoSummaryService.createExcel(examInfoSummaryList,userId);
        // 将Excel文件作为响应发送给客户端
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=records.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
        return WebResult.ok().message("导出成功");
    }

    @GetMapping("/viewExamName")
    public WebResult viewExamName() {
        List<String> examNameList  =  examInfoSummaryService.listObjs(new QueryWrapper<ExamInfoSummary>().lambda().select(ExamInfoSummary::getName).orderByDesc(ExamInfoSummary::getUpdateTime), Object::toString);
        return WebResult.ok().data(examNameList);
    }

    /**
     * 缺少从错题列表里将错题的作答情况取出一并返回的功能
     */
    @GetMapping("/wrongMain")
    public WebResult viewWrongMain(Integer user_id,Long page,Long pageSize) {
        List<WrongQuestion> examNameList  =  examInfoSummaryService.viewWrongMain(user_id,page,pageSize);
        return WebResult.ok().data(examNameList);
    }
}
