package com.jqmk.examsystem.mapper;

import com.jqmk.examsystem.entity.TestPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 试卷，及其考试规则愚得分规则 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface TestPaperMapper extends BaseMapper<TestPaper> {

    @Select("")
    String selectBankName(Integer id);
}
