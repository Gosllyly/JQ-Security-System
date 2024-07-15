package com.jqmk.examsystem.dto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 试卷，及其考试规则愚得分规则
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Data
public class TestPaperDto implements Serializable {

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
     * 考试时长（分钟）
     */
    private Integer duration;


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
     * 试卷是否允许重复做，0代表允许，1代表不允许
     */
    private Integer canRedo;

    /**
     * 是否限时，默认0代表限时，1代表不限时
     */
    private Integer timeLimited;

}
