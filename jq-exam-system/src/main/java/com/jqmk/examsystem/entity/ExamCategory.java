package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 考试类别
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@TableName("exam_category")
public class ExamCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 当前类别名
     */
    private String categoryName;

    /**
     * 父类别id，顶级类别的父为0
     */
    private Integer parentId;

    /**
     * 是否被删除，默认0，代表未被删除，1代表被删除
     */
    @TableLogic
    private Integer deleted;

//    private LocalDateTime createTime;
//
    private LocalDateTime updateTime;


}
