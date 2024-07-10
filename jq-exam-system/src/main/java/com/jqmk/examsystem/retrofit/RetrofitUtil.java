package com.jqmk.examsystem.retrofit;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 17:10 <br/>
 */
@Slf4j
public class RetrofitUtil {

    /**
     * 获取响应体详情
     *
     * @param call
     * @param <T>
     * @return
     */
    public static <T> T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.code() != 200) {
                log.info("异常请求返回 response:" + response.body().toString() );
                throw new IllegalStateException("三方 web 服务端异常");
            }
            return response.body();
        } catch (IOException e) {
            log.error("调用 Retrofit API 失败. request={}", call, e);
            throw new IllegalStateException("调用 Retrofit API 失败");
        }
    }
}