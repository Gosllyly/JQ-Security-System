package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.entity.Question;

/**
 * <p>
 * 试题 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface QuestionService extends IService<Question> {

    Page selectQueryPage(String stem, Integer type, Long page, Long pageSize);

    Page selectBankByPage(Integer questionBankId, Long page, Long pageSize);
}
