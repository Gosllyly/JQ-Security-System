package com.jqmk.examsystem.dto.mas;

import lombok.Data;

/**
 * @ClassName MasLoginRespDto
 * @Author tian
 * @Date 2024/7/11 8:31
 * @Description
 */
@Data
public class MasLoginRespDto {
    private String data;
    private String info;
    private int State;
    private int code;
    //private boolean IsSuccess;
    //private Object PagerInfo;


    @Data
    public static class UserInfo {
        String Id;
        String UserName;
        String TrueName;
        String Token;
        String ImageUrl;
    }
}
