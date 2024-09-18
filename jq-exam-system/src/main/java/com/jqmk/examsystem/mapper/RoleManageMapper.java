package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.entity.RoleManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoleManageMapper  extends BaseMapper<RoleManage> {
    /**
     * 从所有角色的 include_people 中移除指定的用户
     *
     * @param person 要移除的用户
     */
    @Update("UPDATE role_manage " +
            "SET include_people = TRIM(BOTH ',' FROM REPLACE(CONCAT(',', include_people, ','), CONCAT(',', #{person}, ','), ',')) " +
            "WHERE FIND_IN_SET(#{person}, include_people) > 0")
    void removePersonFromAllRoles(@Param("person") String person);


}
