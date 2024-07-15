package com.jqmk.examsystem.retrofit.dto;

import com.jqmk.examsystem.enums.RiskType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 16:46 <br/>
 * description: 定义调用 python 画像系统 rest 接口的响应体
 */
@Data
public class PyPredictionsResp {
    // 返回的预测结果的采用 map 结构，key 为 employeeId，value 是对应员工的预测结果列表
    private Map<String, List<Prediction>> predictions;

    @Data
    @Builder
    static public class Prediction {
        private LocalDate date;
        private RiskType riskType;
        private String reason;
    }
}
