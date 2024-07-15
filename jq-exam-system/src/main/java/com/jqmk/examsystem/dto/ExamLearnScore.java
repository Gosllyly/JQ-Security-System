package com.jqmk.examsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ExamLearnScore
 * @Author tian
 * @Date 2024/6/25 15:48
 * @Description 考试学分实体类
 */
@Data
public class ExamLearnScore {

    private String username;

    private String deptName;

    /**
     * 试卷名称（获取途径）
     */
    private String name;

    /**
     * 本次考试获取学分
     */
    private Integer obtainLearningScore;
//    /**
//     * 总获取学分
//     */
//    private Integer credits;

    /**
     * 考试完成时间（获取学分的时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
}
