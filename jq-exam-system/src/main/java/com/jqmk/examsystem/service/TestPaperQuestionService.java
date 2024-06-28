package com.jqmk.examsystem.service;

import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.entity.TestPaperQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 保存试卷和试题的映射关系 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface TestPaperQuestionService extends IService<TestPaperQuestion> {

    List<ExamQuestion> startExam(Integer id);
}
