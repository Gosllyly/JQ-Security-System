package com.jqmk.examsystem.mapper;

import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 试题 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select id from question where question_bank_id = #{questionBankId} and type = #{type} ORDER BY RAND() limit #{number}")
    List<Integer> selectQuestionId(Integer questionBankId, Integer number, Integer type);

    @Select("select id,stem,type,options from question where id in (${replace}) ORDER BY type")
    List<ExamQuestion> selectQuestion(String replace);
}
