package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

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
     * 考试开始时间
     */
    private String startTime;

    /**
     * 考试结束时间
     */
    private String endTime;

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
