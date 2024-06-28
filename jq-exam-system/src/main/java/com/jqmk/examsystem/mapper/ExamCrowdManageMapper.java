package com.jqmk.examsystem.mapper;

import com.jqmk.examsystem.entity.ExamCrowdManage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 记录分配好的考试人群大类和每一类具体的人，在后续进行问卷下发时使用 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface ExamCrowdManageMapper extends BaseMapper<ExamCrowdManage> {

}
