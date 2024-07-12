package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName TestPaperChallengeDto
 * @Author tian
 * @Date 2024/7/4 9:18
 * @Description
 */
@Data
public class TestPaperChallengeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷名称
     */
    private String name;


    /**
     * 及格线
     */
    private Integer passScore;
    /**
     * 考试时长（分钟）
     */
    private Integer duration;
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
     * 考试介绍
     */
    private String examIntroduce;
}
