package com.jqmk.examsystem.ExamTimeoutTask;

import com.jqmk.examsystem.entity.ExamInfoSummary;
import com.jqmk.examsystem.mapper.ExamInfoSummaryMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ExamTimeoutTask
 * @Author 肥肥的华仔
 * @Date 2024/7/14 20:45
 * @Description 每隔一个小时检查是否有超时的答卷，并强制提交
 */
@Component
public class ExamTimeoutTask {

    @Resource
    private ExamInfoSummaryMapper examInfoSummaryMapper;

    @Scheduled(fixedRate = 60*60*1000) // 每小时执行一次
    public void checkExamTimeout() {
        LocalDateTime now = LocalDateTime.now();
        //查找超时的答卷
        List<ExamInfoSummary> timeoutRecords = examInfoSummaryMapper.findTimeoutRecords(now);

        for (ExamInfoSummary record : timeoutRecords) {
            // 执行强制提交逻辑
            Long id =  record.getId();
            Integer record_id = id.intValue();
            Integer learningScore = 0;
            Integer learningTime = 0;
            //记为不合格
            examInfoSummaryMapper.updateTestPaper(record_id,learningScore,learningTime,2);
        }
    }

}
