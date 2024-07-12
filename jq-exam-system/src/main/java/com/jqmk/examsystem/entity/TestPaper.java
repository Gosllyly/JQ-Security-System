package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 试卷，及其考试规则愚得分规则
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "test_paper",autoResultMap = true)
public class TestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 考试类别id(叶子类别)
     */
    private Integer examCategoryId;

    /**
     * 题库id
     */
    private Integer questionBankId;

    /**
     * 考试时长（分钟）
     */
    private Integer duration;

    /**
     * 单选题个数
     */
    private Integer singleChoiceNum;

    /**
     * 每个单选题得分
     */
    private Integer singleChoiceScore;

    /**
     * 多选题个数
     */
    private Integer multiChoiceNum;

    /**
     * 每个多选题得分
     */
    private Integer multiChoiceScore;

    /**
     * 判断选择题个数
     */
    private Integer judgeChoiceNum;

    /**
     * 每个判断选择题得分
     */
    private Integer judgeChoiceScore;

    /**
     * 及格线
     */
    private Integer passScore;

    /**
     * 参加考试的人群id列表
     */
    private String examCrowdIds;
    /**
     * 参加考试的人群id列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> examCrowdId;

    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 考试结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 状态，默认0代表正常，1代表禁用
     */
    @TableLogic
    private Integer status;

    /**
     * 试卷是否允许重复做，0代表允许，1代表不允许
     */
    private Integer canRedo;

    /**
     * 试卷允许重复做的次数，-1代表无限次
     */
    private Integer redoNum;

    /**
     * 是否限时，默认0代表限时，1代表不限时
     */
    private Integer timeLimited;

    /**
     * 是否记录学时，默认0代表不记录，1代表记录
     */
    private Integer learningTimeRecorded;

    /**
     * 考试通过后可获得学时数
     */
    private Integer learningTime;

    /**
     * 是否记录学分，默认0代表不记录，1代表记录
     */
    private Integer learningScoreRecorded;

    /**
     * 考试通过后可获得学分数
     */
    private Integer learningScore;

    /**
     * 考试介绍
     */
    private String examIntroduce;

    /**
     * 奖励分数（一次性及格）
     */
    private Integer reward;
    /**
     * 区别是不是挑战答题，默认0代表不是，1代表是
     */
    private Integer noChallenge;
//
//    private LocalDateTime createTime;
//
    private LocalDateTime updateTime;


}
