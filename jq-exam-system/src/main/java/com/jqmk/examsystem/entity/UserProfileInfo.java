package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName UserProfileInfo
 * @Author tian
 * @Date 2024/7/15 13:55
 * @Description 用户画像实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_profile_data")
public class UserProfileInfo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String employeeId;

    private String username;

    @TableField(exist = false)
    private String image;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate time;

    private String level;

    private Integer score;

    private String reason;

}
