package com.jqmk.examsystem.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.jqmk.examsystem.dto.PortraitPredictionsReqDto;
import com.jqmk.examsystem.dto.PortraitPredictionsRespDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.retrofit.PortraitApi;
import com.jqmk.examsystem.retrofit.RetrofitUtil;
import com.jqmk.examsystem.retrofit.dto.PyPredictionsResp;
import com.jqmk.examsystem.service.PortraitPredictionService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 18:01 <br/>
 */
@Service
public class PortraitPredictionServiceImpl implements PortraitPredictionService {

    @Resource
    private PortraitApi portraitApi;

    @Override
    public PortraitPredictionsRespDto query(PortraitPredictionsReqDto reqDto) {
        WebResult result = RetrofitUtil.execute(portraitApi.obtainEmployeePredications(buildRequestBody(reqDto)), WebResult.class);
        PyPredictionsResp pyPredictionsResp = JSONUtil.toBean(JSON.toJSONString(result.getData()), PyPredictionsResp.class);

        // 根据返回信息填充最终的 PortraitPredictionsRespDto
        PortraitPredictionsRespDto pprDto = new PortraitPredictionsRespDto();
        Map<String, PortraitPredictionsRespDto.FatPrediction> predictions = new HashMap<>();
        pprDto.setPredictions(predictions);
        Map<String, List<PyPredictionsResp.Prediction>> pyPredictions = pyPredictionsResp.getPredictions();
        if (!pyPredictions.isEmpty()) {
            for (String employeeId : pyPredictions.keySet()) {
                PortraitPredictionsRespDto.FatPrediction fatPrediction = PortraitPredictionsRespDto.FatPrediction.builder()
                        // todo 这里需要查询数据库, 关联用户的头像 url 和姓名
                        .imageUrl(UUID.randomUUID() + "_IMG_URL")
                        .employeeId(employeeId)
                        .name(String.format("关联数据库中的员工 %s 的姓名", employeeId))
                        .build();
                List<PortraitPredictionsRespDto.SonPrediction> details = new ArrayList<>();
                for (PyPredictionsResp.Prediction value : pyPredictions.get(employeeId)) {
                    PortraitPredictionsRespDto.SonPrediction detail = PortraitPredictionsRespDto.SonPrediction.builder()
                            .date(value.getDate())
                            .riskType(value.getRiskType())
                            .reason(value.getReason())
                            .build();
                    details.add(detail);
                }
                fatPrediction.setDetails(details);
                predictions.put(employeeId, fatPrediction);
            }
        }
        return pprDto;
    }

    /**
     * 构建 retrofit 专用的请求体
     *
     * @param data
     * @return
     */
    private RequestBody buildRequestBody(Object data) {
        String json = JSON.toJSONString(data);
        return RequestBody.create(MediaType.parse("application/json"), json.getBytes(StandardCharsets.UTF_8));
    }
}
