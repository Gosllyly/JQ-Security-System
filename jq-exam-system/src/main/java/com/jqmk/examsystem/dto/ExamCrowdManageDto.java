package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ExamCrowdManage
 * @Author tian
 * @Date 2024/7/5 10:34
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "exam_crowd_manage",autoResultMap = true)
public class ExamCrowdManageDto implements Serializable {

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
    private List<String> includeDeptCodes;

    /**
     * 考试人群包含的工种
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeJobType;

    /**
     * 考试人的user列表
     */
    private String includePeoples;

    private String riskPeople;

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
