package com.jqmk.examsystem.service;

import com.jqmk.examsystem.dto.PortraitPredictionsReqDto;
import com.jqmk.examsystem.dto.PortraitPredictionsRespDto;

/**
 * author: Goslly <br/>
 * since:  2024/7/9 17:59 <br/>
 */
public interface PortraitPredictionService {
    /**
     * 获取指定员工+日期对应的预测结果
     * @param reqDto
     * @return
     */
    PortraitPredictionsRespDto query(PortraitPredictionsReqDto reqDto);
}
