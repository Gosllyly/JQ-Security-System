package com.jqmk.examsystem.retrofit;

import com.jqmk.examsystem.dto.WebResult;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 15:45 <br/>
 * description: 定义调用画像系统 rest 接口的 api
 */
public interface PortraitApi {

    @POST("warning/prediction/mock_python_web")
    Call<WebResult> obtainEmployeePredications(@Body RequestBody body);
}
