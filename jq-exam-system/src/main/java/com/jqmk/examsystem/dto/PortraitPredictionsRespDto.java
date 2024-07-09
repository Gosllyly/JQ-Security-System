package com.jqmk.examsystem.dto;

import com.jqmk.examsystem.enums.RiskType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 17:43 <br/>
 */
@Data
public class PortraitPredictionsRespDto {
    // key 为 employeeId，value 为预测结果列表
    private Map<String, FatPrediction> predictions;

    /**
     * 返回给前端的数据需要带上员工姓名和员工图片
     */
    @Data
    @Builder
    public static class FatPrediction {
        private String employeeId;
        private String name;
        private String imageUrl;
        private List<SonPrediction> details;
    }

    @Data
    @Builder
    public static class SonPrediction {
        private LocalDate date;
        private RiskType riskType;
        private String reason;
    }
}
