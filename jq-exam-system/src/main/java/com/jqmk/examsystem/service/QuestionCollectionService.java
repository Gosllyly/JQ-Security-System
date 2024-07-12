package com.jqmk.examsystem.service;

import com.jqmk.examsystem.entity.QuestionCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏题目，在收藏界面查看题目所属题库，以及题目的详细信息 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface QuestionCollectionService extends IService<QuestionCollection> {

    void collection(QuestionCollection questionCollection);

    void removeCollection(QuestionCollection questionCollection);
}
