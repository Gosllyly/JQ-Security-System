package com.jqmk.examsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jqmk.examsystem.entity.ExamCrowdManage;

import java.util.List;

/**
 * <p>
 * 记录分配好的考试人群大类和每一类具体的人，在后续进行问卷下发时使用 服务类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
public interface ExamCrowdManageService extends IService<ExamCrowdManage> {

    List<String> selectNames(List<Integer> examCrowdId);
}
