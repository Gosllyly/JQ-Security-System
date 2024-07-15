package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 存储用户答卷的详细信息以及考试的分数，时间，错题集 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface ExamInfoSummaryMapper extends BaseMapper<ExamInfoSummary> {

    @Select("SELECT ex.id,`user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time,ex.score,sum(ar.answer_correct+ar.answer_wrong+ar.no_reply) as answerCount, " +
            "ar.answer_correct,ar.answer_wrong,ar.no_reply,ex.exam_results,timediff(ex.end_time,ex.start_time) as unavailable FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) " +
            " and test_paper.no_challenge=#{noChallenge} GROUP BY ar.id limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectMain(Integer userId,Integer noChallenge, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) and test_paper.no_challenge=#{noChallenge}")
    Integer countMain(Integer userId,Integer noChallenge);

    @Select("SELECT ex.id,`user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time,ex.score,sum(ar.answer_correct+ar.answer_wrong+ar.no_reply) as answerCount, " +
            "ar.answer_correct,ar.answer_wrong,ar.no_reply,ex.exam_results,timediff(ex.end_time,ex.start_time) as unavailable FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime} ) AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) " +
            "AND (#{name} IS NULL OR ex.name = #{name}) AND (#{examResults} IS NULL OR ex.exam_results = #{examResults}) AND (#{deptName} IS NULL OR user.dept_name = #{deptName}) AND (#{jobType} IS NULL OR user.job_type = #{jobType}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) and test_paper.no_challenge=#{noChallenge} GROUP BY ar.id limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectCondition(Integer userId, String startTime, String endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge, Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime} ) AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) " +
            "AND (#{name} IS NULL OR ex.name = #{name}) AND (#{examResults} IS NULL OR ex.exam_results = #{examResults}) AND (#{deptName} IS NULL OR user.dept_name = #{deptName}) AND (#{jobType} IS NULL OR user.job_type = #{jobType}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) and test_paper.no_challenge=#{noChallenge} ")
    Integer countCondition(Integer userId, String startTime, String endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge);
    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,SUM(test_paper.learning_score) as credits ,ex.end_time " +
            "FROM `user`,exam_info_summary as ex,test_paper where ex.user_id =user.id GROUP BY user.username,ex.`name` order by end_time desc  limit ${page},${pageSize} ")
    List<ExamLearnScore> viewInterface(Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where ex.user_id =user.id GROUP BY user.username,ex.`name` limit 1")
    Integer viewInterfaceCount();

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,ex.end_time FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> selectLearnScore(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id ")
    Integer selectLearnScoreCount(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,sum(ex.obtain_learning_score) as obtainLearningScore, ex.end_time FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> learnScoreCondition(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id GROUP BY user.username,ex.`name` limit 1 ")
    Integer learnScoreConditionCount(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time " +
            "FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` order by end_time desc limit ${page},${pageSize} ")
    List<ExamLearnTime> learnTimeInterface(Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` ")
    Integer learnTimeInterfaceCount();

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time  FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize);

    @Select("SELECT  count(*) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id ")
    Integer selectLearnTimeCount(String deptName, String username, String name);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,SUM(ex.obtain_learning_time) as obtainLearningTime,ex.end_time,SUM(test_paper.learning_time) as learningTime FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id GROUP BY user.id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id")
    Integer learnTimeConditionCount(String deptName, String username, String name);

    @Insert("INSERT INTO exam_info_summary(`user_id`, `test_paper_id`,`name`,`exam_category_id`)" +
            "SELECT #{userId},#{id},test_paper.`name`,test_paper.exam_category_id FROM test_paper where test_paper.id = #{id} ")
    void insertNewRecord(Integer userId, Integer id);

    @Select("select id from exam_info_summary where user_id = #{userId} and test_paper_id = #{id} order by update_time desc limit 1")
    Integer selectId(Integer userId, Integer id);

    @Update("UPDATE exam_info_summary SET `user_answers` = #{userAnswer} WHERE id = #{newId}")
    void updateUserAnswer(Integer newId, String userAnswer);

    @Update("UPDATE exam_info_summary SET score = score+#{singleChoiceScore} WHERE id = #{id}")
    void updateScore(Integer singleChoiceScore,Integer id);
    @Update("UPDATE exam_info_summary SET score = score-#{judgeChoiceScore} WHERE id = #{examSummary}")
    void subtractScore(Integer judgeChoiceScore, Integer examSummary);

    @Select("select error_question_ids from exam_info_summary WHERE id = #{examSummary}")
    String selectWrongId(Integer examSummary);

    @Update("UPDATE exam_info_summary SET `error_question_ids` = #{wrongList} WHERE id = #{examSummary}")
    void updateWrongId(String wrongList, Integer examSummary);
    @Select("select count(error_question_ids) from exam_info_summary WHERE id = #{examSummary}")
    Integer selectCountWrongId(Integer examSummary);

    @Select("SELECT " +
            "  CASE " +
            "    WHEN exam_info_summary.score > test_paper.pass_score THEN '1' " +
            "    ELSE '2' " +
            "  END AS result " +
            "FROM " +
            "  exam_info_summary,test_paper WHERE exam_info_summary.id = #{examSummary} and exam_info_summary.test_paper_id=test_paper.id")
    Integer equalsScore(Integer examSummary);

    @Update("UPDATE exam_info_summary SET `end_time` = NOW(), `exam_results` = #{examResults},`obtain_learning_score` = #{learningScore}, " +
            "`obtain_learning_time` = #{learningTime}, `redo_num` = redo_num+1 WHERE `id` = #{examSummary}")
    void updateTestPaper(Integer examSummary, Integer learningScore, Integer learningTime, Integer examResults);
    @Select("SELECT ex.id,`user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time,ex.score,sum(ar.answer_correct+ar.answer_wrong+ar.no_reply) as answerCount, " +
            "ar.answer_correct,ar.answer_wrong,ar.no_reply,ex.exam_results,timediff(ex.end_time,ex.start_time) as unavailable FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id and test_paper.no_challenge=#{noChallenge} AND (#{userId} IS NULL OR user.id = #{userId}) GROUP BY ar.id ")
    List<ExamRecordDto> exportAll(Integer userId,Integer noChallenge);

    @Select("SELECT eis.* " +
            "FROM exam_info_summary eis " +
            "JOIN test_paper tp ON eis.test_paper_id = tp.id " +
            "WHERE eis.end_time IS NULL " +
            "  AND TIMESTAMPDIFF(MINUTE, eis.start_time, #{now}) > tp.duration; ")
    List<ExamInfoSummary> findTimeoutRecords(LocalDateTime now);

}
