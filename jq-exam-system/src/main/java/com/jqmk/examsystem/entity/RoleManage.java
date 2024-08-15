package com.jqmk.examsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色实体
 * </p>
 *
 * @author fjh
 * @since 2024-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "role_manage",autoResultMap = true)
public class RoleManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id",  type = IdType.INPUT)
    private String roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 该角色包含的部门code列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeDeptCodes;

    /**
     * 该角色包含的工种
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeJobType;

    /**
     * 包含的user列表
     */
    private String includePeople;

    /**
     * 是否被删除，默认是0(未被删除)，1(已删除)
     */
    @TableLogic
    private Integer deleted;

    private LocalDateTime updateTime;

}