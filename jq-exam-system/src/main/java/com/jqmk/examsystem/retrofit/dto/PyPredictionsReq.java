package com.jqmk.examsystem.retrofit.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 16:45 <br/>
 * description: 定义调用 python 画像系统 rest 接口的请求体
 */
@Data
public class PyPredictionsReq {
    private List<Employee> employees;

    @Data
    public static class Employee {
        // 员工id
        private String employeeId;
        // 制定获取哪些日期的预警数据
        private List<LocalDate> dates;
    }
}
