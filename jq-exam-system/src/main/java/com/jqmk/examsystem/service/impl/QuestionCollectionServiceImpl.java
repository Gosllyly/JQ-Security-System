package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.entity.QuestionCollection;
import com.jqmk.examsystem.mapper.QuestionCollectionMapper;
import com.jqmk.examsystem.service.QuestionCollectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 收藏题目，在收藏界面查看题目所属题库，以及题目的详细信息 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class QuestionCollectionServiceImpl extends ServiceImpl<QuestionCollectionMapper, QuestionCollection> implements QuestionCollectionService {

    @Resource
    private QuestionCollectionMapper questionCollectionMapper;

    @Override
    public void collection(Integer userId, Integer questionId) {
        questionCollectionMapper.addCollection(userId,questionId);
    }
}
