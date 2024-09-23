package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.QuestionBank;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 题库 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface QuestionBankService extends IService<QuestionBank> {

    void importData(MultipartFile file,Integer questionBankId) throws JsonProcessingException;

    List<Question> exportQuestionBank(Integer questionBankId);

    XSSFWorkbook createExcel(List<Question> records, Integer questionBankId);
}
