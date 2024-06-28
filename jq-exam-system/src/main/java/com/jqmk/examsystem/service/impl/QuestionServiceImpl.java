package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionService;
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

    @Override
    public Page selectQueryPage(String stem, Integer type, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().like(stem!=null,"stem",stem)
                .eq(type!=null,"type",type)).getRecords());
        return records;
    }

    @Override
    public Page selectBankByPage(Integer questionBankId, Long page, Long pageSize) {
        Page<Question> questionPage = new Page<>(page,pageSize);
        questionPage.addOrder(new OrderItem("update_time",false));
        Page records =  questionPage.setRecords(questionMapper.selectPage(questionPage,new QueryWrapper<Question>().eq("question_bank_id",questionBankId)).getRecords());
        return records;
    }
}
