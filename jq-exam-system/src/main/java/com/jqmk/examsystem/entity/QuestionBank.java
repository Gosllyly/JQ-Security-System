package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 题库
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question_bank")
public class QuestionBank implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属题库id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属题库名称
     */
    private String bankName;


//    private LocalDateTime createTime;
//
//    private LocalDateTime updateTime;

}
