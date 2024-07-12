package com.jqmk.examsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MasInteractiveConfig
 * @Author tian
 * @Date 2024/7/11 8:27
 * @Description
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mas")
public class MasInteractiveConfig {
    /**
     * 提供服务的 context——path
     */
    private String serviceAddress;
    /**
     * 登录接口地址
     */
    private String loginUrl;
    /**
     * 登录用户名
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;

    /**
     * 获取全量用户信息的接口地址
     */
    private String userListUrl;
}
