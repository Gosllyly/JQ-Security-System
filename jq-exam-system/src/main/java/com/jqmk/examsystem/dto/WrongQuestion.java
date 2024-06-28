package com.jqmk.examsystem.dto;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName WrongQuestion
 * @Author tian
 * @Date 2024/6/26 10:22
 * @Description 错题的实体类
 */
@Data
public class WrongQuestion {
    /**
     * 题干
     */
    private String stem;

    /**
     * 选项，使用 json 表示，举例: {"A":"XXXXX","B":"YYYYY"}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject options;

    /**
     * 正确答案，使用 json 数组表示，举例：["A","B"]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> correctOptions;

    /**
     * 错误答案，使用 json 数组表示，举例：["A","B"]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> wrongOptions;

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
    private Integer questionBankId;
    /**
     * 考试完成时间
     */
    private LocalDateTime endTime;
}
