package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 记录分配好的考试人群大类和每一类具体的人，在后续进行问卷下发时使用
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "exam_crowd_manage",autoResultMap = true)
public class ExamCrowdManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 考试人群名称
     */
    private String crowdName;

    /**
     * 考试人群包含的部门code列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeDepCodes;

    /**
     * 考试人群包含的工种
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeJobType;

    /**
     * 考试人的user列表
     */
    private String includePeoples;

    /**
     * 是否被删除，默认是0(未被删除)，1(已删除)
     */
    @TableLogic
    private Integer deleted;

//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
    private LocalDateTime updateTime;


}
