package com.jqmk.examsystem.dto.userProfile;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserProfileInfoDto
 * @Author tian
 * @Date 2024/7/18 8:51
 * @Description 用户画像主界面展示所需实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileInfoDto {

    private String employeeId;

    private String username;

    //private String imgFile;
}
