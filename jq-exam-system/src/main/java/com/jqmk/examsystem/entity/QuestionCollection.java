package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("question_collection")
public class QuestionCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 所属题库id
     */
    private Integer questionBankId;

    /**
     * 问题类型1(单选)，2(多选)，3(判断)
     */
    private Boolean type;

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

    private LocalDateTime collectionTime;


}
