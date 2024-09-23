package com.jqmk.examsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RiskPeoplesDto
 * @Author tian
 * @Date 2024/9/20 9:06
 * @Description 风险查询实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskPeoplesDto {

    //筛选风险用户名单的时间
    private String time;

    //筛选姓氏
    private String name;

}
