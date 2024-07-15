package com.jqmk.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jqmk.examsystem.entity.ExamCrowdManage;
import com.jqmk.examsystem.mapper.ExamCrowdManageMapper;
import com.jqmk.examsystem.service.ExamCrowdManageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录分配好的考试人群大类和每一类具体的人，在后续进行问卷下发时使用 服务实现类
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Service
public class ExamCrowdManageServiceImpl extends ServiceImpl<ExamCrowdManageMapper, ExamCrowdManage> implements ExamCrowdManageService {

}
