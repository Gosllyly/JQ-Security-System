package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 保存试卷和试题的映射关系
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_paper_question")
public class TestPaperQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷的id
     */
    private Integer testPaperId;

    /**
     * 题目 id
     */
    private List<Integer> questionId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
