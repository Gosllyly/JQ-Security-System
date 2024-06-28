package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.entity.TestPaperQuestion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 保存试卷和试题的映射关系 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface TestPaperQuestionMapper extends BaseMapper<TestPaperQuestion> {

    @Insert("insert ignore into test_paper_question (test_paper_id,question_id) values (#{id},#{questionIds})")
    void insertData(Integer id, String questionIds);
}
