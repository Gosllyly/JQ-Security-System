package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName SelectPeoplesDto
 * @Author tian
 * @Date 2024/7/18 11:01
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "exam_crowd_manage",autoResultMap = true)
public class SelectPeoplesDto {

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

    //筛选风险用户名单的时间
    private String time;

    //筛选风险用户名单的等级
    private String risk;

    //筛选姓氏
    private String name;

    //筛选井下违章作业类型的人员名单
    private String type;
}
