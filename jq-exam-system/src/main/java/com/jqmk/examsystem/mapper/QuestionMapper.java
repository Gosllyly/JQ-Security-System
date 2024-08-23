package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.ExamInfoSummaryDto;
import com.jqmk.examsystem.dto.ExamQuestion;
import com.jqmk.examsystem.dto.WrongQuestion;
import com.jqmk.examsystem.dto.export.ExportQuestion;
import com.jqmk.examsystem.entity.Question;
import com.jqmk.examsystem.framwork.config.JsonTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    @Results({
            @Result(property = "options", column = "options", typeHandler = JsonTypeHandler.class),
            @Result(property = "correctOptions", column = "correct_options", typeHandler = JsonTypeHandler.class),
            @Result(property = "errorOptions", column = "error_options", typeHandler = JsonTypeHandler.class)
    })
    @Select("select DISTINCT(stem),id,user_id,question_bank_name,options,correct_options,analysis,type,error_options,create_time from user_error_records " +
            "where user_id =#{userId} GROUP BY create_time,stem ORDER BY create_time DESC limit ${page}, ${pageSize}")
    List<WrongQuestion> selectErrorQuestion(Integer userId,Long page,Long pageSize);
    @Select("select COUNT(stem) from user_error_records where user_id =#{userId} ")
    Integer countErrorQuestion(Integer userId);

    @Insert("INSERT INTO user_error_records(`user_id`, `question_bank_name`,`stem`,`options`,`correct_options`,`analysis`,`type`,`error_options`) " +
            "SELECT DISTINCT #{userId},qb.bank_name,q.stem,q.`options`,q.correct_options,q.analysis,q.type,#{wrong} FROM question as q,question_bank as qb where q.id = #{questionId} and q.question_bank_id = qb.id")
    void addWrongsInfo(String questionId, String wrong,Integer userId);

    @Results({
            @Result(property = "options", column = "options", typeHandler = JsonTypeHandler.class),
            @Result(property = "correctOptions", column = "correct_options", typeHandler = JsonTypeHandler.class),
            @Result(property = "errorOptions", column = "error_options", typeHandler = JsonTypeHandler.class)
    })
    @Select("select DISTINCT(stem),id,user_id,question_bank_name,options,correct_options,analysis,type,error_options,create_time from user_error_records " +
            "where user_id =#{userId} and (#{stem} IS NULL OR stem like '%${stem}%') and (#{type} IS NULL OR type = #{type}) GROUP BY create_time,stem ORDER BY create_time desc limit ${page}, ${pageSize}")
    List<WrongQuestion> errorQuestionCondition(Integer userId, Integer type, String stem, Long page, Long pageSize);
    @Select("select COUNT(stem) from user_error_records where user_id =#{userId} and (#{stem} IS NULL OR stem like '%${stem}%') " +
            "and (#{type} IS NULL OR type = #{type}) ")
    Integer countErrorQuestionCondition(Integer userId, Integer type, String stem);

    @Select("select COUNT(DISTINCT stem) from question where type = 1 and status = 0 and question_bank_id = #{question_bank_id} ")
    Integer countSingleChoiceQuestionByBankId(Integer question_bank_id);

    @Select("select COUNT(DISTINCT stem) from question where type = 2 and status = 0 and question_bank_id = #{question_bank_id} ")
    Integer countMultiChoiceQuestionByBankId(Integer question_bank_id);

    @Select("select COUNT(DISTINCT stem) from question where type = 3 and status = 0 and question_bank_id = #{question_bank_id} ")
    Integer countJudgeQuestionByBankId(Integer question_bank_id);


    @Select("SELECT COUNT(*) FROM question WHERE stem = #{stem} AND JSON_CONTAINS(OPTIONS, #{options})  AND question_bank_id = #{bankId}")
    int countByStemAndOptions(String stem, String options, Integer bankId);

    @Select("SELECT CONCAT(stem, options) as combined FROM question WHERE question_bank_id = #{bankId}")
    List<String> selectCombinedByBankId(Integer bankId);

//    @Results({
//            @Result(property = "options", column = "options", typeHandler = JsonTypeHandler.class),
//            @Result(property = "correctOptions", column = "correct_options", typeHandler = JsonTypeHandler.class),
//    })
    @Select("select stem,options,correct_options,analysis,type,status from question WHERE  question_bank_id = #{questionBankId}")
    List<ExportQuestion> selectByBankId(Integer questionBankId);

    @Select("SELECT COUNT(*) as amount,SUM(type=1) as radio,SUM(type=2) as multiple,SUM(type=3) as judge from question WHERE `status`=0")
    Map<String, Object> countQuestion();

    @Update("UPDATE `question` SET `status` = 0 WHERE id in (${idData})")
    void restoreQuestion(String idData);

    @Select("select count(*) from question where status = 1")
    Integer selectDelCount();

    @Results({
            @Result(property = "options", column = "options", typeHandler = JsonTypeHandler.class),
            @Result(property = "correctOptions", column = "correct_options", typeHandler = JsonTypeHandler.class)
    })
    @Select("select id,stem,options,correct_options,type,analysis from question where status = 1 order by update_time desc limit #{page},#{pageSize}")
    List<ExamQuestion> viewDelQuestion(Long page, Long pageSize);
}
