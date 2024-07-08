package com.jqmk.examsystem.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.dto.QuestionAdd;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.entity.QuestionBank;
import com.jqmk.examsystem.mapper.QuestionBankMapper;
import com.jqmk.examsystem.mapper.QuestionMapper;
import com.jqmk.examsystem.service.QuestionAddService;
import com.jqmk.examsystem.service.QuestionBankService;
import com.jqmk.examsystem.service.QuestionService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 题库 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements QuestionBankService {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionAddService questionAddService;
    @Resource
    private QuestionMapper questionMapper;
    @Override
    public void importData(MultipartFile file,Integer questionBankId)  {
        QuestionAdd question = new QuestionAdd();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0 ) {
                continue;
            }
            Integer type =conventType(row.getCell(0).getStringCellValue());
            String stem = row.getCell(1).getStringCellValue();
            JSONObject options = StringsUtil.strToJson(row.getCell(2).getStringCellValue());
            String correctOptions = StringsUtil.stringCut(row.getCell(3).getStringCellValue());
            String analysis = row.getCell(4).getStringCellValue();
            Integer status = conventStatus(row.getCell(5).getStringCellValue());
            question.setType(type);
            question.setStem(stem);
            question.setOptions(options);
            question.setCorrectOptions(correctOptions);
            question.setAnalysis(analysis);
            question.setStatus(status);
            question.setQuestionBankId(questionBankId);
            questionAddService.save(question);
            System.out.println(question);
        }
    }

    @Override
    public List<Question> exportQuestionBank(Integer questionBankId) {
        List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().select("type","stem","options","correct_options","analysis","status").eq("question_bank_id",questionBankId));
        return questionList;
    }

    public Integer conventType(String type) {
        Integer questionType = null;
        if (type.equals("单选题")) {
            questionType=1;
        }else if (type.equals("多选题")) {
            questionType=2;
        }else {
            questionType=3;
        }
        return questionType;
    }
    public Integer conventStatus(String Statue) {
        Integer questionStatue = null;
        if (Statue.equals("启用")) {
            questionStatue=0;
        }else {
            questionStatue=1;
        }
        return questionStatue;
    }
    @Override
    public XSSFWorkbook createExcel(List<Question> records, Integer questionBankId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int rowNum = 0;
        // 创建表头行并设置字段名
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("题型");
        headerRow.createCell(1).setCellValue("题干");
        headerRow.createCell(2).setCellValue("选项");
        headerRow.createCell(3).setCellValue("答案");
        headerRow.createCell(4).setCellValue("解析");
        headerRow.createCell(5).setCellValue("状态");

        // 将数据写入 Excel 文件
        for (Question record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getType());
            row.createCell(1).setCellValue(record.getStem());
            row.createCell(2).setCellValue(record.getOptions().toString());
            row.createCell(3).setCellValue(record.getCorrectOptions().toString());
            row.createCell(4).setCellValue(record.getAnalysis());
            row.createCell(5).setCellValue(record.getStatus());
        }

        return workbook;
    }
}
