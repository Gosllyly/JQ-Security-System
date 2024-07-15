package com.jqmk.examsystem.dto.mas;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MasPersonResBody
 * @Author tian
 * @Date 2024/7/11 8:32
 * @Description
 */
@Data
public class MasPersonResBody {
    private List<MasPerson> rows;
    private int total;
    private int page;
    private int records;
}
