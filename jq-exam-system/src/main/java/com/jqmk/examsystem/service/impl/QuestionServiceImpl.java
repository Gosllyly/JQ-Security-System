package com.jqmk.examsystem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.dto.export.ExportQuestion;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.TestPaper;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.service.TestPaperService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 试题 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private TestPaperService testPaperService;
    @Resource
    private ExamInfoSummaryMapper examInfoSummaryMapper;
    private static String wrongList = null;

    @Override
    public Page selectQueryPage(Integer questionBankId,String stem, Integer type, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().like(stem!=null,"stem",stem)
                .eq( "question_bank_id",questionBankId).eq(type!=null,"type",type)).getRecords());
        return records;
    }

    @Override
    public Page queryQuestionPage(String stem, Integer type, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().like(stem!=null,"stem",stem)
                .eq( "status",0).eq(type!=null,"type",type)).getRecords());
        return records;
    }

    @Override
    public Map<String, Object> viewDelQuestion(Long page, Long pageSize) {
        Integer total = questionMapper.selectDelCount();
        List<ExamQuestion>  questionList = questionMapper.viewDelQuestion((page - 1) * pageSize, pageSize);
        Map<String, Object> res = new HashMap();
        res.put("data", questionList);
        res.put("total", total);
        return res;
    }

    @Override
    public void restoreQuestion(String ids) {
        String idData = StringsUtil.intToStr(ids);
        questionMapper.restoreQuestion(idData);
    }

    @Override
    public Page selectBankByPage(Integer questionBankId, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().eq("question_bank_id",questionBankId).eq("status",0)).getRecords());
        return records;
    }

    @Override
    public Page selectBaseBankByPage(Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().eq("status",0)).getRecords());
        return records;
    }

    @Override
    public void addCurrent(Integer id,Integer testId, String questionId,Integer examSummary) {
        Integer type = questionMapper.selectType(Integer.valueOf(questionId));//题目类型
        TestPaper testPaper = testPaperService.getById(id);//分值
        if (type==1) {//单选
            examInfoSummaryMapper.updateScore(testPaper.getSingleChoiceScore(),examSummary);
        }else if (type==2) {//多选
            examInfoSummaryMapper.updateScore(testPaper.getMultiChoiceScore(),examSummary);
        }else if (type==3) {//判断
            examInfoSummaryMapper.updateScore(testPaper.getJudgeChoiceScore(),examSummary);
        }
    }

    @Override
    public void addWrongs(Integer id,Integer testId, String questionId,Integer examSummary,String wrong,Integer userId) {
//        Integer type = questionMapper.selectType(Integer.valueOf(questionId));//题目类型
//        TestPaper testPaper = testPaperService.getById(id);//分值
        String stem = questionMapper.selectStem(Integer.valueOf(questionId));
        if (questionMapper.countWrongQuestion(stem)<1) {
            questionMapper.addWrongsInfo(questionId, StringsUtil.strToList(wrong),userId);
        }
//        if (type==1) {//单选
//            examInfoSummaryMapper.subtractScore(testPaper.getSingleChoiceScore(),examSummary);
//        }else if (type==2) {//多选
//            examInfoSummaryMapper.subtractScore(testPaper.getMultiChoiceScore(),examSummary);
//        }else if (type==3) {//判断
//            examInfoSummaryMapper.subtractScore(testPaper.getJudgeChoiceScore(),examSummary);
//        }
        if (examInfoSummaryMapper.selectCountWrongId(examSummary)!=0) {
            wrongList = examInfoSummaryMapper.selectWrongId(examSummary);
            System.out.println("查出"+examInfoSummaryMapper.selectWrongId(examSummary));
            wrongList = wrongList+","+questionId;
            examInfoSummaryMapper.updateWrongId(wrongList,examSummary);
        }else {
            wrongList=questionId;
            examInfoSummaryMapper.updateWrongId(wrongList,examSummary);
        }
    }
    @Override
    public boolean ifQuestionCountIsLegal(TestPaper testPaper){
        //防止后续如果需要返回具体是哪一个题型数量超出，三个if分开写
        if (testPaper.getSingleChoiceNum() > questionMapper.countSingleChoiceQuestionByBankId(testPaper.getQuestionBankId())) {
            return false;
        }
        if (testPaper.getMultiChoiceNum() > questionMapper.countMultiChoiceQuestionByBankId(testPaper.getQuestionBankId())) {
            return false;
        }
        if (testPaper.getJudgeChoiceNum() > questionMapper.countJudgeQuestionByBankId(testPaper.getQuestionBankId())) {
            return false;
        }
        return true;
    }

    @Override
    public String selectByBankId(Integer questionBankId,HttpServletResponse response) {
        List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("question_bank_id",questionBankId));
        List<ExportQuestion> exportQuestionList = new ArrayList<>();
        for (Question ech : questionList) {
            ExportQuestion exportQuestion = new ExportQuestion();
            exportQuestion.setStem(ech.getStem());
            exportQuestion.setOptions(StringsUtil.jsonToStr(ech.getOptions().toString()));
            System.out.println(exportQuestion.getOptions());
            exportQuestion.setCorrectOptions(StringsUtil.StingToStr(ech.getCorrectOptions().toString()));
            exportQuestion.setType(ech.getType());
            exportQuestion.setAnalysis(ech.getAnalysis());
            exportQuestion.setStatus(conventStatus(ech.getStatus()));
            exportQuestionList.add(exportQuestion);
        }
        try {
            this.setExcelResponseProp(response);
            EasyExcel.write(response.getOutputStream())
                    .head(ExportQuestion.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("题库")
                    .doWrite(exportQuestionList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }
    private void setExcelResponseProp(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment; filename=question_bank.xlsx");
    }
    public String conventStatus(Integer Statue) {
        String questionStatue = null;
        if (Statue==0) {
            questionStatue="启用";
        }else {
            questionStatue="禁用";
        }
        return questionStatue;
    }
}
