package com.jqmk.examsystem.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 17:18 <br/>
 */
@Data
public class PortraitPredictionsReqDto {
    private List<Employee> employees;

    @Data
    public static class Employee {
        // 员工id
        private String employeeId;
        // 制定获取哪些日期的预警数据
        private List<LocalDate> dates;
    }
}
