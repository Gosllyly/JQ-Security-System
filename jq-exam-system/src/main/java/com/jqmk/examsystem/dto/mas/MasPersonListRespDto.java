package com.jqmk.examsystem.dto.mas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MasPersonListRespDto
 * @Author tian
 * @Date 2024/7/11 8:32
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MasPersonListRespDto {
    MasPersonResBody data;
    /*返回信息*/
    private Integer State;
    /*返回编码，200正常，-100失败*/
    private Integer code;
    /*正常返回true，异常返回false*/
    private String info;
    private Integer page;

    //private PagerInfo PagerInfo;

    @Data
    public static class PagerInfo {
        private int PageIndex;
        private int PageSize;
        private int RowCount;
        private int TotalPages;
    }
}

