package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.entity.TestPaper;
import com.jqmk.examsystem.entity.TestPaperQuestion;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.mapper.TestPaperQuestionMapper;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.service.TestPaperQuestionService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 保存试卷和试题的映射关系 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class TestPaperQuestionServiceImpl extends ServiceImpl<TestPaperQuestionMapper, TestPaperQuestion> implements TestPaperQuestionService {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionService questionService;
    @Resource
    private TestPaperMapper testPaperMapper;
    @Resource
    private TestPaperQuestionMapper testPaperQuestionMapper;

    @Override
    public List<ExamQuestion> startExam(Integer id) {
        //第一步，根据试卷id，查出题库id
        TestPaper tp = testPaperMapper.selectOne(new QueryWrapper<TestPaper>().select().eq("id",id));
        //第二步，从中抽出题目id，保存在数组
        String singleChoice = questionMapper.selectQuestionId(tp.getQuestionBankId(),tp.getSingleChoiceNum(),1).toString();
        String multiChoice = questionMapper.selectQuestionId(tp.getQuestionBankId(),tp.getMultiChoiceNum(),2).toString();
        String judgeChoice = questionMapper.selectQuestionId(tp.getQuestionBankId(),tp.getJudgeChoiceNum(),3).toString();
        String questionIds = singleChoice+multiChoice+judgeChoice;
        //第三步，存入test_paper_question表
        testPaperQuestionMapper.insertData(id,questionIds.replace("][", ", "));
        System.out.println(StringsUtil.stringWipe(questionIds));
        //第四步，根据表中的问题id数组，将问题的详情返回
        List<ExamQuestion> question = questionMapper.selectQuestion(StringsUtil.stringWipe(questionIds));
        return question;
    }
}
