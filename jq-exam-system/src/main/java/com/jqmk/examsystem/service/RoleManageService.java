package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.entity.RoleManage;

import java.util.List;

public interface RoleManageService extends IService<RoleManage> {
    public void removePeopleFromOtherRoles(List<String> includePeople);
}
