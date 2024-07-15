package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName ExamQuestion
 * @Author tian
 * @Date 2024/6/27 15:19
 * @Description 试卷问题实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "question",autoResultMap = true)
public class ExamQuestion {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题干
     */
    private String stem;

    /**
     * 选项，使用 json 表示，举例: {"A":"XXXXX","B":"YYYYY"}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("options")
    private JsonNode options;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode correctOptions;
    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    private Integer type;
    /**
     * 解析
     */
    private String analysis;
}
