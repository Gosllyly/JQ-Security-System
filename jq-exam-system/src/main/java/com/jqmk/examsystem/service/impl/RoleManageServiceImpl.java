package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.entity.RoleManage;
import com.jqmk.examsystem.mapper.RoleManageMapper;
import com.jqmk.examsystem.service.RoleManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManageServiceImpl extends ServiceImpl<RoleManageMapper, RoleManage> implements RoleManageService {

    @Autowired
    private RoleManageMapper roleMapper;

    /**
     * 移除 include_people 中的用户从其他角色中
     *
     * @param includePeople 新角色的 include_people 字段
     */
    public void removePeopleFromOtherRoles(List<String> includePeople) {
        for (String person : includePeople) {
            // 去除其他角色的 include_people 字段中包含该用户的记录
            roleMapper.removePersonFromAllRoles(person.trim());
        }
    }

}
