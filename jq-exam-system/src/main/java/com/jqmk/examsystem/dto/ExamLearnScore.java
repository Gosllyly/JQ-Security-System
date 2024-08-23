package com.jqmk.examsystem.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ExamLearnScore
 * @Author tian
 * @Date 2024/6/25 15:48
 * @Description 考试学分实体类
 */
@Data
public class ExamLearnScore {

    @ExcelProperty("员工姓名")
    @ColumnWidth(value = 18)
    private String username;

    @ExcelProperty("部门")
    private String deptName;

    /**
     * 试卷名称（获取途径）
     */
    @ExcelProperty("试卷名称")
    @ColumnWidth(value = 18)
    private String name;

    /**
     * 本次考试获取学分
     */
    @ExcelProperty("考试获取学分")
    @ColumnWidth(value = 18)
    private Integer obtainLearningScore;

    /**
     * 本次考试实际获取的学时
     */
    @ExcelProperty("实际获取学时")
    @ColumnWidth(value = 18)
    private Integer obtainLearningTime;

//    /**
//     * 总获取学分
//     */
//    private Integer credits;

    /**
     * 考试完成时间（获取学分的时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("考试完成时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(value = 18)
    private LocalDateTime endTime;
}
