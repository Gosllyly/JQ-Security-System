package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @ClassName UserAnswerDto
 * @Author tian
 * @Date 2024/7/8 10:05
 * @Description 用户作答列表
 */
@Data
@TableName(value = "exam_info_summary",autoResultMap = true)
public class UserAnswerDto {

    /**
     * 用户的答案列表，格式: [{"question_id1":answer1},{"question_id2":answer2}]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("userAnswers")
    private JsonNode userAnswers;
}
