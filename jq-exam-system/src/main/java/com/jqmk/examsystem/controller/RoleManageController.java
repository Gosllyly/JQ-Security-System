package com.jqmk.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jqmk.examsystem.dto.RoleManageDto;
import com.jqmk.examsystem.dto.SelectPeoplesDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.RoleManage;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.service.RoleManageService;
import com.jqmk.examsystem.service.UserService;
import com.jqmk.examsystem.utils.StringsUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/role")
public class RoleManageController {

    @Resource
    private RoleManageService roleManageService;
    @Resource
    private UserService userService;

    @GetMapping("/main")
    public WebResult viewMain(@RequestParam Long page, @RequestParam Long pageSize) {
        Page<RoleManage> roleManagePage = new Page<>(page,pageSize);
        Page roleManages =  roleManagePage.setRecords(roleManageService.page(roleManagePage,new QueryWrapper<RoleManage>()
                .orderByDesc("update_time")).getRecords());
        return WebResult.ok().data(roleManages);
    }

    @PostMapping("/add")
    public WebResult addRoleManage(@RequestBody RoleManageDto roleManageDto) {
        RoleManage roleManage = new RoleManage();
        roleManage.setRoleId(roleManageDto.getRoleId());
        roleManage.setRoleName(roleManageDto.getRoleName());
        roleManage.setDescription(roleManageDto.getDescription());
        roleManage.setIncludeJobType(roleManageDto.getIncludeJobType());
        roleManage.setIncludeDeptCodes(roleManageDto.getIncludeDeptCodes());

        String includePeopleStr = StringsUtil.stringRecomNew(roleManageDto.getIncludePeople().toString());
        List<String> includePeopleList = Arrays.asList(includePeopleStr.split(","));

        roleManage.setIncludePeople(includePeopleStr);
        if (roleManageService.count(new QueryWrapper<RoleManage>().eq("role_id",roleManage.getRoleId()))==0) {
            roleManageService.save(roleManage);
            userService.updateUserTable(includePeopleList, roleManage.getRoleId());
            return WebResult.ok().message("创建角色成功");
        }else {
            return WebResult.fail().message("角色重复!");
        }
    }

    @GetMapping("/selectDeptName")
    public WebResult selectDeptName() {
        List<String> deptNameList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getDeptName).eq(User::getDeleteFlag,0), Object::toString);
        Stream<String> distinct = deptNameList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    @GetMapping("/selectJobType")
    public WebResult selectJobType() {
        List<String> jobTypeList =  userService.listObjs(new QueryWrapper<User>().lambda().select(User::getJobType).eq(User::getDeleteFlag,0), Object::toString);
        Stream<String> distinct = jobTypeList.stream().distinct();
        return WebResult.ok().data(distinct);
    }

    @PostMapping ("/selectPeoples")
    public WebResult selectPeoples(@RequestBody SelectPeoplesDto selectPeoplesDto) {
        List<String> users = userService.selectUsersByNames(selectPeoplesDto);
        return WebResult.ok().data(users);
    }

    @PostMapping("/update")
    public WebResult updateRoleManage(@RequestBody RoleManageDto roleManageDto) {
        RoleManage roleManage = new RoleManage();
        roleManage.setRoleId(roleManageDto.getRoleId());
        roleManage.setRoleName(roleManageDto.getRoleName());
        roleManage.setDescription(roleManageDto.getDescription());
        roleManage.setIncludeJobType(roleManageDto.getIncludeJobType());
        roleManage.setIncludeDeptCodes(roleManageDto.getIncludeDeptCodes());

        String includePeopleStr = StringsUtil.stringRecomNew(roleManageDto.getIncludePeople().toString());
        List<String> includePeopleList = Arrays.asList(includePeopleStr.split(","));

        roleManage.setIncludePeople(includePeopleStr);
        if (roleManageService.count(new QueryWrapper<RoleManage>().eq("role_id",roleManage.getRoleId()))!=0) {
            roleManage.setUpdateTime(LocalDateTime.now());
            roleManageService.updateById(roleManage);
            userService.updateUserTable(includePeopleList, roleManage.getRoleId());
            return WebResult.ok().message("更新角色成功");
        }else {
            return WebResult.fail().message("角色重复!");
        }

    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody RoleManage roleManage) {
        roleManageService.removeById(roleManage);
        return WebResult.ok().message("删除成功");
    }

}
