package com.jqmk.examsystem.retrofit;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 16:21 <br/>
 * description: 定义调用画像系统 rest 接口的代理服务
 */
public class PortraitRestProxy {
    private final Retrofit retrofit;
    private final int COMMON_TIMEOUT = 60;

    public PortraitRestProxy(String basicUrl) {

        OkHttpClient retailClient = new OkHttpClient.Builder().
                connectTimeout(COMMON_TIMEOUT, TimeUnit.SECONDS).
                readTimeout(COMMON_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(COMMON_TIMEOUT, TimeUnit.SECONDS).
                callTimeout(COMMON_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(basicUrl)
                .client(retailClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    /**
     * 根据接口类型获取对应的 api service 实例
     *
     * @param restApiClass
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> restApiClass) {
        return retrofit.create(restApiClass);
    }
}
