package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @ClassName WrongQuestion
 * @Author tian
 * @Date 2024/6/26 10:22
 * @Description 错题的实体类
 */
@Data
@TableName(value = "user_error_records",autoResultMap = true)
public class WrongQuestion {


    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

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

    /**
     * 正确答案，使用 json 数组表示，举例：["A","B"]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("correctOptions")
    private JsonNode correctOptions;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    private Integer type;

    /**
     * 所属题库id
     */
    private String questionBankName;
    /**
     * 所属题库id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("errorOptions")
    private JsonNode errorOptions;

    private String createTime;
}
