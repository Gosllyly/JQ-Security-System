package com.jqmk.examsystem.retrofit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 16:20 <br/>
 */
@Configuration
public class RetrofitConfiguration {

    // retrofit 访问画像 web 服务的基地址
    @Value("${retrofit.portrait.basic.url}")
    private String portraitBasicUrl;


    @Bean
    public PortraitApi portraitApi() {
        return new PortraitRestProxy(portraitBasicUrl).createService(PortraitApi.class);
    }
}
