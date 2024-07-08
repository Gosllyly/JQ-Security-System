package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.ExamInfoSummaryDto;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.framwork.config.JsonTypeHandler;
import org.apache.ibatis.annotations.*;

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

    @Results({
            @Result(property = "options", column = "options", typeHandler = JsonTypeHandler.class),
            @Result(property = "correctOptions", column = "correct_options", typeHandler = JsonTypeHandler.class)
    })
    @Select("select id,stem,type,options,correct_options,analysis from question where id in (${replace}) ORDER BY type")
    List<ExamQuestion> selectQuestion(String replace);

    @Select("select question_id from test_paper_question where test_paper_id = #{id}")
    String selectQuestionIds(Integer id);

    @Results({
            @Result(property = "daan", column = "correct_options", typeHandler = JsonTypeHandler.class)
    })
    @Select("select correct_options from question where id = #{id}")
    String selectCurrent(Integer id);

    @Insert("INSERT INTO answers_records(`user_id`, `exam_info_summary_id`,`test_paper_id`) VALUES (#{userId},#{examSummary},#{id})")
    void insertRecord(Integer id, Integer examSummary, Integer userId);

    @Select("select id from answers_records where test_paper_id=#{id} and user_id = #{userId} order by time desc limit 1")
    Integer selectId(Integer id, Integer userId);

    @Select("UPDATE answers_records SET answer_correct=answer_correct+1 WHERE `id` = #{testId}")
    void addCurrent(Integer testId);
    @Select("UPDATE answers_records SET answer_wrong=answer_wrong+1 WHERE `id` = #{testId}")
    void addWrongs(Integer testId);
    @Select("UPDATE answers_records SET no_reply=no_reply+1 WHERE `id` = #{testId}")
    void addNoReply(Integer testId);

    @Select("select type from question where id=#{questionId}")
    Integer selectType(Integer questionId);

    @Results({
            @Result(property = "userAnswers", column = "user_answers", typeHandler = JsonTypeHandler.class)
    })
    @Select("select user_answers,exam_results,score,obtain_learning_score,obtain_learning_time,timediff(end_time,start_time) as unavailable " +
            "from exam_info_summary where id = #{examSummary}")
    List<ExamInfoSummaryDto> selectUserAnswer(Integer examSummary);
}
