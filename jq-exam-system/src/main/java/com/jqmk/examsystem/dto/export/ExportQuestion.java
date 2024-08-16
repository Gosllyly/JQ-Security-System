package com.jqmk.examsystem.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.jqmk.examsystem.component.QuestionTypeConverter;
import lombok.Data;

/**
 * @ClassName ExportQuestion
 * @Author tian
 * @Date 2024/7/23 9:35
 * @Description 导出题库的问题实体类
 */
@Data
@ContentRowHeight(15)
public class ExportQuestion {

    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    @ExcelProperty(value = "问题类型",converter = QuestionTypeConverter.class)
    private Integer type;

    /**
     * 题干
     */
    @ExcelProperty("题干")
    @ColumnWidth(value = 80)
    private String stem;

    /**
     * 选项，使用 json 表示，举例: {"A":"XXXXX","B":"YYYYY"}
     */
    @ExcelProperty("选项")
    @ColumnWidth(value = 30)
    private String options;

    /**
     * 正确答案，使用 json 数组表示，举例：["A","B"]
     */
    @ExcelProperty("正确答案")
    private String correctOptions;

    /**
     * 解析
     */
    @ExcelProperty("解析")
    private String analysis;

    /**
     * 题目状态是否被禁用0(启用)，1(禁用)
     */
    @ExcelProperty("题目状态")
    private String status;
}
