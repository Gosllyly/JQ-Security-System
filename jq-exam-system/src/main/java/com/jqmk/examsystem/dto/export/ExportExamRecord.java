package com.jqmk.examsystem.dto.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.jqmk.examsystem.component.ResultTypeConverter;
import lombok.Data;

/**
 * @ClassName ExportExamRecord
 * @Author tian
 * @Date 2024/7/29 8:56
 * @Description 导出考试成绩实体
 */
@Data
public class ExportExamRecord {
    @ExcelIgnore
    private Integer id;

    @ExcelProperty("名字")
    private String username;

    @ExcelProperty("工种")
    private String jobType;

    @ExcelProperty("部门")
    private String deptName;

    /**
     * 考试名称
     */
    @ExcelProperty("考试名称")
    @ColumnWidth(value = 12)
    private String name;

    /**
     * 考试成绩结果，1代表及格，2不及格，0未参考
     */
    @ExcelProperty(value = "成绩结果",converter = ResultTypeConverter.class)
    @ColumnWidth(value = 12)
    private Integer examResults;
    /**
     * 答题总数
     */
    @ExcelProperty("总答题数")
    @ColumnWidth(value = 12)
    private Integer answerCount;
    /**
     * 答题正确数
     */
    @ExcelProperty("回答正确数目")
    @ColumnWidth(value = 12)
    private Integer answerCorrect;
    /**
     * 答题错误数
     */
    @ExcelProperty("回答错误数目")
    @ColumnWidth(value = 12)
    private Integer answerWrong;
    /**
     * 答题未作答
     */
    @ExcelProperty("未作答数目")
    @ColumnWidth(value = 12)
    private Integer noReply;
    /**
     * 分数
     */
    @ExcelProperty("分数")
    private Integer score;
    /**
     * 考试开始时间
     */
    @ExcelProperty("考试开始时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(value = 18)
    private String startTime;
    /**
     * 用时
     */
    @ExcelProperty("考试用时")
    @DateTimeFormat("HH:mm:ss")
    private String unavailable;

    /**
     * 考试完成时间
     */
    @ExcelProperty("考试结束时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(value = 18)
    private String endTime;
}
