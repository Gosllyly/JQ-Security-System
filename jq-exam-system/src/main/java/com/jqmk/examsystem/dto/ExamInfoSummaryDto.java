package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @ClassName ExamInfoSummaryDto
 * @Author tian
 * @Date 2024/7/6 10:59
 * @Description
 */
@Data
@TableName(value = "exam_info_summary",autoResultMap = true)
public class ExamInfoSummaryDto {

    /**
     * 用户的答案列表，格式: [{"question_id1":answer1},{"question_id2":answer2}]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("userAnswers")
    private JsonNode userAnswers;
    /**
     * 考试成绩结果，1代表及格，2不及格，0未参考
     */
    private Integer examResults;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 本次考试获得的学分
     */
    private Integer obtainLearningScore;

    /**
     * 本次考试获得的学时
     */
    private Integer obtainLearningTime;
    /**
     * 用时
     */
    private String unavailable;
}
