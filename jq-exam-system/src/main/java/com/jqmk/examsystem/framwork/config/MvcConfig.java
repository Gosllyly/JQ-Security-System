package com.jqmk.examsystem.framwork.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jqmk.examsystem.framwork.aop.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName MvcConfig
 * @Author tian
 * @Date 2024/6/5 13:51
 * @Description
 */
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 添加jwt拦截器，并指定拦截路径
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/verify")
                .excludePathPatterns("/register/verify")
                .excludePathPatterns("/test/**");
    }

    /**
     * jwt拦截器
     *
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
    @Bean
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter converter=new FastJsonHttpMessageConverter();
        return converter;
    }
}