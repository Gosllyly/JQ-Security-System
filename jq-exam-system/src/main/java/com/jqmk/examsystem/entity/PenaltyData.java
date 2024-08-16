package com.jqmk.examsystem.entity;

import lombok.Data;

/**
 * @ClassName PenaltyData
 * @Author tian
 * @Date 2024/8/15 16:00
 * @Description 违章数据实体类
 */
@Data
public class PenaltyData {

    private String violationDate;

    private String violationFacts;

    private Integer penaltyAmount;
}
