package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("exam_info_summary")
public class ExamInfoSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试记录的id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 考试用户的id
     */
    private Integer userId;

    /**
     * 试卷的id
     */
    private Integer testPaperId;

    /**
     * 考试类别id
     */
    private Integer examCategoryId;

    /**
     * 考试名称
     */
    private String name;

    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;

    /**
     * 考试完成时间
     */
    private LocalDateTime endTime;

    /**
     * 用户的答案列表，格式: [{"question_id1":answer1},{"question_id2":answer2}]
     */
    private String userAnswers;

    /**
     * 考试成绩结果，1代表及格，2不及格，0未参考
     */
    private Integer examResults;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 本次考试获得的学分
     */
    private Integer obtainLearningScore;

    /**
     * 本次考试获得的学时
     */
    private Integer obtainLearningTime;

    /**
     * 累计重复做答次数
     */
    private Integer redoNum;

    /**
     * 用户的考试错题列表, json 数组表示 [101,102,111]
     */
    private String errorQuestionIds;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
