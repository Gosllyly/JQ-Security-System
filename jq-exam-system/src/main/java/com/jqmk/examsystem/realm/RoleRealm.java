package com.jqmk.examsystem.realm;

import com.jqmk.examsystem.entity.RoleManage;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("dddddddddddddddddddddd:" + authenticationToken);
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.getByName(token.getUsername());
        if(user != null){
            System.out.println("--------用户：" + user.getUsername());
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }
       // userService.login(token.getUsername(), String.valueOf(token.getPassword()));
        return null;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 从PrincipalCollection中获取用户的主体
        User user0 = (User) principals.getPrimaryPrincipal();
        String username = user0.getUsername();
        System.out.println("角色名：：：：：" + user0.getRoleId());

        // 从数据库获取用户信息
        User user = userService.getByName(username);

        // 创建AuthorizationInfo对象
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 根据用户的roleId获取对应的角色信息
        RoleManage role = userService.getRoleByRoleId(user.getRoleId());

        // 添加角色到AuthorizationInfo
        authorizationInfo.addRole(role.getRoleName());

        // 根据role的authDegree字段分配权限
        int authDegree = role.getAuthDegree();  // 获取authDegree的值

        // 根据authDegree值，分配不同的权限
        switch (authDegree) {
            case 1: // 超级管理员
                authorizationInfo.addRole("super_admin");
                authorizationInfo.addRole("general_admin");
                authorizationInfo.addRole("general_user");
                break;
            case 2: // 普通管理员
                authorizationInfo.addRole("general_admin");
                authorizationInfo.addRole("general_user");
                break;
            case 3: // 普通用户
                authorizationInfo.addRole("general_user");
                break;
            default:
                // 可以处理其他情况，比如没有匹配的authDegree
                System.out.println("未知的authDegree: " + authDegree);
                break;
        }
//        // 添加其他权限，例如通过逗号分隔的authUrls字段
//        String[] authUrls = role.getAuthUrls().split(","); // 假设authUrls是逗号分隔的字符串
//        for (String url : authUrls) {
//            authorizationInfo.addStringPermission(url);
//        }

        return authorizationInfo;
    }

}
