package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
        long startTime = System.currentTimeMillis();
        List<QuestionAdd> questionList = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sheet sheet = workbook.getSheetAt(0);

        int skippedCount = 0;
        int insertedCount = 0;

        // 为避免短时间多次重复访问数据库，先把题库中的题缓存出来（使用由题干和选项组成的字符串来判定去重）
        List<String> existingQuestions = questionMapper.selectCombinedByBankId(questionBankId);

        //预处理，去除空格
        ListIterator<String> iterator = existingQuestions.listIterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            temp = StringsUtil.removeWhitespace(temp);
            iterator.set(temp);
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Integer type = conventType(row.getCell(0).getStringCellValue());
            String stem = row.getCell(1).getStringCellValue();
            String optionsStr = row.getCell(2).getStringCellValue();
            String options = StringsUtil.strToJson(optionsStr);
            String correctOptions = StringsUtil.stringCut(row.getCell(3).getStringCellValue());
            String analysis = row.getCell(4).getStringCellValue();
            Integer status = conventStatus(row.getCell(5).getStringCellValue());

            // 拼接题目和选项
//            String combined = stem  + options.toJSONString();
//
//                // 检查题目是否已经存在（使用缓存）
//                if (existingQuestions.contains(combined)) {
//                    skippedCount++;
//                    continue;
//            }

            QuestionAdd question = new QuestionAdd();
            question.setType(type);
            question.setStem(stem);
            question.setOptions(options);
            question.setCorrectOptions(correctOptions);
            question.setAnalysis(analysis);
            question.setStatus(status);
            question.setQuestionBankId(questionBankId);
            questionList.add(question);
            insertedCount++;
        }

        // 批量插入
        questionAddService.saveBatch(questionList);

        long endTime = System.currentTimeMillis();
        System.out.println("导入总耗时: " + (endTime - startTime) + " ms");
        System.out.println("插入题目个数： " + insertedCount);
        System.out.println("重复题目个数: " + skippedCount);
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
