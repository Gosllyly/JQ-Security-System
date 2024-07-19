package com.jqmk.examsystem.dto.userProfile;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName UserProfileSelectDot
 * @Author tian
 * @Date 2024/7/18 13:43
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserProfileSelectDto {

    /**
     * 考试人群包含的部门code列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeDeptCodes;

    /**
     * 考试人群包含的工种
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> includeJobType;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> names;
}
