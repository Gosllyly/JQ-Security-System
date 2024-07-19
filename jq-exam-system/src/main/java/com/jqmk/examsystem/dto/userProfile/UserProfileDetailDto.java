package com.jqmk.examsystem.dto.userProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserProfileDetailDto
 * @Author tian
 * @Date 2024/7/19 8:43
 * @Description 用户画像详情实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDetailDto {


    private String employeeId;

    private String username;

    private String level;
}
