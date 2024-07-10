package com.jqmk.examsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.ExamInfoSummaryDto;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.ExamInfoSummaryService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    private ExamInfoSummaryMapper examInfoSummaryMapper;
    @Resource
    private QuestionMapper questionMapper;


    /**
     * 考试成绩查询的主界面
     *
     */
    @GetMapping("/main")
    @Transactional
    public WebResult viewMain(@RequestParam(required = false) Integer userId, @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 0;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.viewMain(userId,noChallenge,page,pageSize));
    }

    /**
     * 按条件进行查询
     * @return
     */
    @GetMapping("/select")
    @Transactional
    public WebResult selectCondition(@RequestParam(required = false) Integer userId, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) Integer examCategoryId,@RequestParam(required = false) String name,@RequestParam(required = false) Integer examResults,
                                     @RequestParam(required = false) String deptName,@RequestParam(required = false) String  jobType,@RequestParam(required = false) String username,
                                     @RequestParam Long page, @RequestParam Long pageSize) {
        Integer noChallenge = 0;//挑战答题
        return WebResult.ok().data(examInfoSummaryService.selectCondition(userId,startTime,endTime,examCategoryId,name,examResults,deptName,jobType,username,noChallenge,page,pageSize));
    }

    /**
     * 同上缺少对答题情况的判断和插入，目前只返回姓名，时间，总分数，结果
     */
    @GetMapping ("/exportAll")
    public WebResult exportQuestionBank(@RequestParam(required = false) Integer userId, HttpServletResponse response) throws IOException {
        Integer noChallenge=0;
        List<ExamRecordDto> examInfoSummaryList = examInfoSummaryService.exportExamRecord(userId,noChallenge);
        // 使用ExportService创建并填充Excel文件
        XSSFWorkbook workbook = examInfoSummaryService.createExcel(examInfoSummaryList);
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
        Stream<String> distinct = examNameList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    /**
     * 错题集主界面
     */
    @GetMapping("/wrongMain")
    public WebResult viewWrongMain(Integer userId,Long page,Long pageSize) {
        return WebResult.ok().data(examInfoSummaryService.viewWrongMain(userId,page,pageSize));
    }

    @GetMapping("/wrongSelect")
    public WebResult selectWrong(Integer userId,@RequestParam(required = false) Integer type,@RequestParam(required = false) String stem,Long page,Long pageSize) {
        return WebResult.ok().data(examInfoSummaryService.selectWrong(userId,type,stem,page,pageSize));
    }

    @GetMapping("/correctExam")//这个id是test_paper的id
    public WebResult correctExam(@RequestParam Integer id,@RequestParam Integer userId,@RequestParam List<String> userAnswer) {
        return WebResult.ok().data(examInfoSummaryService.correctingTestPaper(id,userId,userAnswer));
    }

    @GetMapping("/viewById")
    public WebResult viewExamInfoSummary(Integer id) {
        ExamInfoSummary examInfoSummary = examInfoSummaryMapper.selectById(id);
        String questionIds = questionMapper.selectQuestionIds(examInfoSummary.getTestPaperId());
        List<ExamQuestion> question = questionMapper.selectQuestion(StringsUtil.stringWipe(questionIds));
        List<ExamInfoSummaryDto> examInfoSummaryList = questionMapper.selectUserAnswer(id);
        Map<String, Object> res = new HashMap();
        res.put("question", question);
        res.put("answer", examInfoSummaryList);
        return WebResult.ok().data(res);
    }
}
