package com.jqmk.examsystem.dto;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>
 * 试题
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@TableName(value = "question",autoResultMap = true)
public class QuestionAdd implements Serializable {

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
    private String options;

    /**
     * 正确答案，使用 json 数组表示，举例：["A","B"]
     */
    private String correctOptions;

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
     * 题目状态是否被禁用0(启用)，1(禁用)
     */
    @TableLogic
    private Integer status;

//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
//    /**
//     * 修改时间
//     */
//    private LocalDateTime updateTime;
}

