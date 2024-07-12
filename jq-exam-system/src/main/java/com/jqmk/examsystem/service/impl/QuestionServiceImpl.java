package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.TestPaper;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.service.TestPaperService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public Page selectBankByPage(Integer questionBankId, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().eq("question_bank_id",questionBankId)).getRecords());
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
        Integer type = questionMapper.selectType(Integer.valueOf(questionId));//题目类型
        TestPaper testPaper = testPaperService.getById(id);//分值
        questionMapper.addWrongsInfo(questionId, StringsUtil.strToList(wrong),userId);
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
}
