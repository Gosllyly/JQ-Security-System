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
import java.util.stream.Collectors;
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
        roleManage.setAuthDegree(roleManageDto.getAuthDegree());

        String includePeopleStr = StringsUtil.stringRecomNew(roleManageDto.getIncludePeople().toString());
        List<String> includePeopleList = Arrays.asList(includePeopleStr.split(","));

        roleManage.setIncludePeople(includePeopleStr);
        if (roleManageService.count(new QueryWrapper<RoleManage>().eq("role_id",roleManage.getRoleId()))==0) {
            //先删除旧角色中的名字，保证一个名字只有一个角色
            roleManageService.removePeopleFromOtherRoles(includePeopleList);
            userService.updateUserTable(includePeopleList, roleManage.getRoleId());
            roleManageService.save(roleManage);

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
        roleManage.setAuthDegree(roleManageDto.getAuthDegree());

        String includePeopleStr = StringsUtil.stringRecomNew(roleManageDto.getIncludePeople().toString());
        List<String> includePeopleList = Arrays.asList(includePeopleStr.split(","));

        roleManage.setIncludePeople(includePeopleStr);
        if (roleManageService.count(new QueryWrapper<RoleManage>().eq("role_id",roleManage.getRoleId()))!=0) {
            // 获取数据库中的旧的角色信息，包括旧的人员名单
            RoleManage oldRoleManage = roleManageService.getById(roleManage.getRoleId());
            String oldIncludePeopleStr = oldRoleManage.getIncludePeople();
            List<String> oldIncludePeopleList = Arrays.asList(oldIncludePeopleStr.split(","));

            // 找出旧名单中存在，但新名单中不再包含的人员
            List<String> peopleToRemoveRole = oldIncludePeopleList.stream()
                    .filter(person -> !includePeopleList.contains(person))
                    .collect(Collectors.toList());

            // 对这些人员，将他们的角色更改为 "general_user"
            if (!peopleToRemoveRole.isEmpty()) {
                userService.updateUserTable(peopleToRemoveRole, "general_user");
            }

            roleManage.setUpdateTime(LocalDateTime.now());
            userService.updateUserTable(includePeopleList, roleManage.getRoleId());
            roleManageService.updateById(roleManage);
            return WebResult.ok().message("更新角色成功");
        }else {
            return WebResult.fail().message("角色不存在!");
        }

    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody RoleManage roleManagedto) {

        String roleId = roleManagedto.getRoleId();
        RoleManage roleManage = roleManageService.getOne(new QueryWrapper<RoleManage>()
                                                          .eq("role_id", roleId.trim()));
        //获取需要删除角色包含的用户列表
        String includePeople = roleManage.getIncludePeople();
        if (includePeople != null && !includePeople.isEmpty()) {

            String includePeopleStr = StringsUtil.stringRecomNew(includePeople.toString());
            List<String> includePeopleList = Arrays.asList(includePeopleStr.split(","));

            // 更新这些用户的 role_id 为 "general_user"
            userService.updateUserTable(includePeopleList,"general_user");
        }
        roleManageService.removeById(roleManage);
        return WebResult.ok().message("删除成功");
    }

}
