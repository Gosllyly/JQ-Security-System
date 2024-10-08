package com.jqmk.examsystem.controller;


import com.jqmk.examsystem.component.TimedTask;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URISyntaxException;

/**
 * <p>
 * 用户表，存储用户基本信息，根据网页，是通过身份证和姓名进行唯一性确认 前端控制器
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private TimedTask timedTask;

    /**
     * 用户登录验证
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public WebResult login(@RequestParam String userName, @RequestParam String password) {
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            //shiro帮我们匹配密码什么的，我们只需要把东西传给它，它会根据RoleRealm里认证方法设置的来验证
            user.login(token);
            return WebResult.ok().data(userService.selectInfo(userName));
        } catch (UnknownAccountException e) {
            return WebResult.fail().message("用户名不存在");
        } catch (DisabledAccountException e) {
            return WebResult.fail().message("账号未启用");
        } catch (IncorrectCredentialsException e) {
            return WebResult.fail().message("密码错误");
        } catch (Throwable e) {
            return WebResult.fail().message("未知错误");
        }

//        Map<String, Object> result = userService.login(userName, password);
//        int keyNumber = (int) result.get("code");
//        switch (keyNumber) {
//            case -1:
//                return WebResult.fail().message("用户名不存在");
//            case 0:
//                return WebResult.fail().message("密码错误");
//            case 1:
//                return WebResult.ok().data(result);
//            default:
//                return WebResult.fail().message("未知错误");
//        }
    }

    @PostMapping("/register")
    public WebResult registerUser(@RequestBody User userInfo) {
        userService.save(userInfo);
        return WebResult.ok().message("注册成功");
    }

    @PostMapping("/update")
    public WebResult updateUser(@RequestBody User userInfo) {
        if (userService.getById(userInfo.getId())!=null) {
            userService.updateById(userInfo);
            return WebResult.ok().message("更新成功");
        }else {
            return WebResult.fail().message("查无此id"+userInfo.getId());
        }
    }

    @GetMapping("/view")
    public WebResult getUserSelf(@RequestParam Long id) {
        return WebResult.ok().data(userService.getById(id));
    }

    @GetMapping("/sync")
    public WebResult sync() throws URISyntaxException {
        timedTask.syncEmployeeInfoMas();
        return WebResult.ok();
    }

    @GetMapping("/score")
    public WebResult score()  {
        timedTask.CalculateScore();
        return WebResult.ok();
    }

}
