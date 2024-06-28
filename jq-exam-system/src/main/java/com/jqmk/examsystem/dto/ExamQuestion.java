package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName ExamQuestion
 * @Author tian
 * @Date 2024/6/27 15:19
 * @Description 试卷问题实体类
 */
@Data
@TableName(value = "question",autoResultMap = true)
public class ExamQuestion {

    /**
     * 题干
     */
    private String stem;

    /**
     * 选项，使用 json 表示，举例: {"A":"XXXXX","B":"YYYYY"}
     */
    //@TableField(typeHandler = JacksonTypeHandler.class)
    private String options;

    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    private Integer type;
}
