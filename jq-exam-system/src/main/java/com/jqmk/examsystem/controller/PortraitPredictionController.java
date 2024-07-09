package com.jqmk.examsystem.controller;

import com.jqmk.examsystem.dto.PortraitPredictionsReqDto;
import com.jqmk.examsystem.dto.PortraitPredictionsRespDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.enums.RiskType;
import com.jqmk.examsystem.retrofit.dto.PyPredictionsReq;
import com.jqmk.examsystem.retrofit.dto.PyPredictionsResp;
import com.jqmk.examsystem.service.PortraitPredictionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 17:15 <br/>
 */
@RestController
@RequestMapping("/warning/prediction")
public class PortraitPredictionController {

    @Resource
    private PortraitPredictionService portraitPredictionService;

    @PostMapping("/query")
    public WebResult query(@RequestBody PortraitPredictionsReqDto dto) {
        PortraitPredictionsRespDto obtain = portraitPredictionService.query(dto);
        return WebResult.ok().data(obtain);
    }

    /**
     * 模拟 python web系统
     *
     * @param dto
     * @return
     */
    @PostMapping("/mock_python_web")
    public WebResult mockPythonWeb(@RequestBody PyPredictionsReq dto) {
        List<PyPredictionsReq.Employee> employees = dto.getEmployees();
        PyPredictionsResp resp = new PyPredictionsResp();
        Map<String, List<PyPredictionsResp.Prediction>> predictions = new HashMap<>();
        resp.setPredictions(predictions);
        if (!employees.isEmpty()) {
            for (PyPredictionsReq.Employee employee : employees) {
                List<LocalDate> dates = employee.getDates();
                if (!dates.isEmpty()) {
                    List<PyPredictionsResp.Prediction> employeePredictions = new ArrayList<>();
                    for (LocalDate date : dates) {

                        PyPredictionsResp.Prediction item = PyPredictionsResp.Prediction.builder()
                                .date(date)
                                .riskType(RiskType.values()[(int) (System.currentTimeMillis() % 3)])
                                .build();
                        employeePredictions.add(item);
                    }
                    predictions.put(employee.getEmployeeId(), employeePredictions);
                }
            }
        }
        return WebResult.ok().data(resp);
    }
}
