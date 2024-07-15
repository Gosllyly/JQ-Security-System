package com.jqmk.examsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ExamLearnTime
 * @Author tian
 * @Date 2024/6/26 9:01
 * @Description 考试学时查询实体类
 */
@Data
public class ExamLearnTime {

    private String username;

    private String deptName;

    /**
     * 试卷名称（获取途径）
     */
    private String name;

    /**
     * 本次考试可获取的学时
     */
    private Integer learningTime;
    /**
     * 本次考试实际获取的学时
     */
    private Integer obtainLearningTime;

    /**
     * 考试完成时间（获取学分的时间）
     */
    private LocalDateTime endTime;
}