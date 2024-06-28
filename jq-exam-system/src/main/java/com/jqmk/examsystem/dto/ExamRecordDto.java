package com.jqmk.examsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ExamRecordDto
 * @Author tian
 * @Date 2024/6/24 13:43
 * @Description
 */
@Data
public class ExamRecordDto {

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
    private Integer answerWrongs;
    /**
     * 答题未作答
     */
    private Integer noReply;
    /**
     * 答题未作答
     */
    private Integer score;
    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;

    /**
     * 考试完成时间
     */
    private LocalDateTime endTime;

}
