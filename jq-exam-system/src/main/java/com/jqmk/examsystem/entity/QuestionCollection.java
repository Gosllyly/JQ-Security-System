package com.jqmk.examsystem.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收藏题目，在收藏界面查看题目所属题库，以及题目的详细信息
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "question_collection",autoResultMap = true)
public class QuestionCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;
    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 所属题库
     */
    private String questionBankName;

    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    private Integer type;

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
     * 解析
     */
    private String analysis;

    private LocalDateTime collectionTime;
}
