package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName ExamCategoryDto
 * @Author tian
 * @Date 2024/7/4 8:47
 * @Description 类别管理的展示实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("exam_category")
public class ExamCategoryDto implements Serializable {

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

}
