package com.jqmk.examsystem.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @ClassName ExamRecordDto
 * @Author tian
 * @Date 2024/6/24 13:43
 * @Description
 */
@Data
public class ExamRecordDto {

    @ExcelIgnore
    private Integer id;

    private String username;

    private String jobType;

    private String deptName;

    /**
     * 考试名称
     */
    private String name;

    /**
     * 考试成绩结果，1代表及格，2不及格，0未参考
     */
    private Integer examResults;
    /**
     * 答题总数
     */
    private Integer answerCount;
    /**
     * 答题正确数
     */
    private Integer answerCorrect;
    /**
     * 答题错误数
     */
    private Integer answerWrong;
    /**
     * 答题未作答
     */
    private Integer noReply;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 考试开始时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String startTime;
    /**
     * 用时
     */
    @DateTimeFormat("HH:mm:ss")
    private String unavailable;

    /**
     * 考试完成时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(value = 18)
    private String endTime;

}
