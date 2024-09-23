package com.jqmk.examsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName UserRoleDto
 * @Author tian
 * @Date 2024/9/19 15:44
 * @Description 用户权限返回体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
public class UserRoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String roleId;

    private String username;

    private String password;

    private String jobType;

    private String idCard;
    private Integer cardNo;

    private String deptName;
    private String employeeId;
    private String imgFile;
    @TableLogic
    private Integer deleteFlag;

    private LocalDateTime createDate;

    private LocalDateTime updateTime;

    private Integer authDegree;
}