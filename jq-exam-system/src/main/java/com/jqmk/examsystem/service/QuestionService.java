package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.dto.export.ExportQuestion;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.TestPaper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 试题 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface QuestionService extends IService<Question> {

    Page selectQueryPage(Integer questionBankId,String stem, Integer type, Long page, Long pageSize);

    Page selectBankByPage(Integer questionBankId, Long page, Long pageSize);

    void addCurrent(Integer id,Integer testId, String questionId,Integer examSummary);

    void addWrongs(Integer id,Integer testId, String questionId,Integer examSummary,String wrong,Integer userId);

    boolean ifQuestionCountIsLegal(TestPaper testPaper);

    String selectByBankId(Integer questionBankId,HttpServletResponse response);
}
