package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.dto.export.ExportExamRecord;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
            " and test_paper.no_challenge=#{noChallenge} GROUP BY ar.id ORDER BY ex.end_time desc  limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectMain(Integer userId,Integer noChallenge, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) and test_paper.no_challenge=#{noChallenge}")
    Integer countMain(Integer userId,Integer noChallenge);

    @Select("SELECT ex.id,`user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time,ex.score,sum(ar.answer_correct+ar.answer_wrong+ar.no_reply) as answerCount, " +
            "ar.answer_correct,ar.answer_wrong,ar.no_reply,ex.exam_results,timediff(ex.end_time,ex.start_time) as unavailable FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime} ) " +
            "AND (#{name} IS NULL OR ex.name = #{name}) AND (#{examResults} IS NULL OR ex.exam_results = #{examResults}) AND (#{deptName} IS NULL OR user.dept_name = #{deptName}) AND (#{jobType} IS NULL OR user.job_type = #{jobType}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) and test_paper.no_challenge=#{noChallenge} GROUP BY ar.id ORDER BY ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectCondition(Integer userId, String startTime, String endTime, String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge, Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,test_paper,exam_info_summary as ex,answers_records as ar " +
            "where user.id = ex.user_id and user.id = ar.user_id and test_paper.id = ar.test_paper_id and ex.test_paper_id = ar.test_paper_id and ar.exam_info_summary_id = ex.id AND (#{userId} IS NULL OR user.id = #{userId}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime} )  " +
            "AND (#{name} IS NULL OR ex.name = #{name}) AND (#{examResults} IS NULL OR ex.exam_results = #{examResults}) AND (#{deptName} IS NULL OR user.dept_name = #{deptName}) AND (#{jobType} IS NULL OR user.job_type = #{jobType}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) and test_paper.no_challenge=#{noChallenge} ")
    Integer countCondition(Integer userId, String startTime, String endTime,String name, Integer examResults, String deptName, String jobType, String username,Integer noChallenge);
    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,ex.obtain_learning_time,ex.end_time " +
            "FROM `user`,exam_info_summary as ex where ex.user_id =user.id GROUP BY user.username,ex.id order by end_time desc  limit ${page},${pageSize} ")
    List<ExamLearnScore> viewInterface(Long page, Long pageSize);
    @Select("SELECT COUNT(*) FROM `user`,exam_info_summary as ex where ex.user_id =user.id")
    Integer viewInterfaceCount();

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,ex.obtain_learning_time,ex.end_time " +
            "FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) " +
            "and ex.user_id =user.id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> selectLearnScore(String deptName, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time  " +
            "FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id " +
            "and test_paper.id = ex.test_paper_id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize);


    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id ")
    Integer selectLearnScoreCount(String deptName, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,sum(ex.obtain_learning_score) as obtainLearningScore,sum(ex.obtain_learning_time) as obtainLearningTime, ex.end_time FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id group by user.username order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> learnScoreCondition(String deptName, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT COUNT(DISTINCT ex.name)  FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) and ex.user_id =user.id ")
    Integer learnScoreConditionCount(String deptName, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time " +
            "FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` order by end_time desc limit ${page},${pageSize} ")
    List<ExamLearnTime> learnTimeInterface(Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` ")
    Integer learnTimeInterfaceCount();

    @Select("SELECT  count(*) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id ")
    Integer selectLearnTimeCount(String deptName, String username, String name);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,SUM(ex.obtain_learning_time) as obtainLearningTime,ex.end_time,SUM(test_paper.learning_time) as learningTime FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id GROUP BY ex.name order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize);

    @Select("SELECT COUNT(DISTINCT ex.name) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id")
    Integer learnTimeConditionCount(String deptName, String username, String name);

    @Insert("INSERT INTO exam_info_summary(`user_id`, `test_paper_id`,`name`)" +
            "SELECT #{userId},#{id},test_paper.`name` FROM test_paper where test_paper.id = #{id} ")
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
            "    WHEN exam_info_summary.score >= test_paper.pass_score THEN '1' " +
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
    List<ExportExamRecord> exportAll(Integer userId, Integer noChallenge);

    @Select("SELECT eis.* " +
            "FROM exam_info_summary eis " +
            "JOIN test_paper tp ON eis.test_paper_id = tp.id " +
            "WHERE eis.end_time IS NULL " +
            "  AND TIMESTAMPDIFF(MINUTE, eis.start_time, #{now}) > tp.duration; ")
    List<ExamInfoSummary> findTimeoutRecords(LocalDateTime now);

    @Select("SELECT sum(exam_results!=1) as nopass,sum(exam_results=1) as pass FROM `exam_info_summary` WHERE test_paper_id=#{testId} ")
    Map<String, Object> passRatePie(Integer testId);

    @Select("SELECT sum(exam_results!=1) as nopass,sum(exam_results=1) as pass,DATE(end_time) as date FROM `exam_info_summary` " +
            "WHERE DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(end_time) GROUP BY DATE(end_time)  ORDER BY date ")
    List<Map<String, Object>> passRateHistogram();

    @Select("SELECT LENGTH(t.exam_crowd_ids)-LENGTH(REPLACE(t.exam_crowd_ids,',',''))+1 as num,count(DISTINCT es.user_id) as examNum " +
            "FROM `exam_info_summary` as es,test_paper as t WHERE es.test_paper_id=#{testId} and es.test_paper_id=t.id")
    Map<String, Object> takeTestNum(Integer testId);

    @Select("SELECT count(id) as total,DATE(end_time) as date FROM `exam_info_summary` WHERE DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(end_time) GROUP BY DATE(end_time)  ORDER BY date")
    List<Map<String, Object>> takeTestNumHistogram();

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low FROM `user_profile_data` WHERE DATE(creat_time) = CURDATE()")
    Map<String, Object> riskPie();

    @Select("SELECT CONCAT(convert(sum(`level`!='低风险')*100/count(*),decimal(10,2)),'') as percent,DATE(creat_time) as date FROM `user_profile_data` " +
            "WHERE DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(creat_time) GROUP BY DATE(creat_time) ORDER BY date ")
    List<Map<String, Object>> riskPercentage();

    @Select("SELECT sum(`level`='高风险') as high ,sum(`level`='中风险') as medium,sum(`level`='低风险') as low ,`user`.dept_name as deptName " +
            "FROM `user_profile_data` as up,`user` WHERE up.username=`user`.username and DATE(up.creat_time) = CURDATE() " +
            "GROUP BY user.dept_name ORDER BY low desc LIMIT 10")
    List<Map<String, Object>> riskHistogram();


    @Select("SELECT MAX(es.score) as max,MIN(es.score) as min,ROUND(AVG(es.score),0) as avg,COUNT(DISTINCT es.user_id) as joinNum,es.name,length(t.exam_crowd_ids)+1 - length(REPLACE(t.exam_crowd_ids,',','')) AS allNum,t.id as id " +
            "from exam_info_summary AS es,test_paper as t WHERE t.id=es.test_paper_id GROUP BY es.name")
    List<Map<String, Object>> scoreDetail();

    @Select("SELECT es.score,`user`.username from exam_info_summary as es,user WHERE user.id=es.user_id and es.test_paper_id =#{id} GROUP BY es.user_id ORDER BY es.update_time desc ")
    List<Map<String, Object>> selectExamDetails(Integer id);

    @Select("select " +
            "sum(case when score between 0 and 59 then 1 else 0 end) as failing, " +
            "sum(case when score between 60 and 69 then 1 else 0 end) as competency, " +
            "sum(case when score between 70 and 79 then 1 else 0 end) as proficiency , " +
            "sum(case when score between 80 and 89 then 1 else 0 end) as good, " +
            "sum(case when score between 90 and 100 then 1 else 0 end) as excellent " +
            "from exam_info_summary where test_paper_id=#{id}")
    Map<String, Object> selectTableData(Integer id);

    @Select("SELECT DISTINCT user.username from exam_info_summary as es,user WHERE es.user_id=user.id and es.test_paper_id=#{id}")
    List<String> selectExamPeople(Integer id);

    @Select("select exam_crowd_ids from test_paper where id = #{id}")
    List<String> selectAllExamPeople(Integer id);

    @Select("SELECT es.`name`,user.dept_name as deptName,MAX(es.score) AS max,MIN(es.score) as min,ROUND(AVG(es.score),0) as avg " +
            "FROM exam_info_summary as es,user where es.user_id=user.id and es.test_paper_id=#{id} and (#{time} IS NULL OR DATE(es.end_time) = #{time} ) " +
            "GROUP BY user.dept_name ORDER BY avg desc limit 0,#{size}")
    List<Map<String, Object>> selectExamPercentage(String time, Integer id,Integer size);

    @Select("select test_paper_id from exam_info_summary order by update_time desc limit 0,1")
    Integer selectTestId();

    @Select("SELECT CONCAT(convert(sum(es.exam_results!=1)*100/count(es.exam_results),decimal(10,2)),'') as nopass,CONCAT(convert(sum(es.exam_results=1)*100/count(es.exam_results),decimal(10,2)),'') as pass,user.dept_name as deptName from exam_info_summary as es,test_paper as t,`user` " +
            "WHERE es.test_paper_id=t.id and t.id=#{testId} and (#{time} IS NULL OR DATE(es.end_time) = #{time}) and user.id=es.user_id group by user.dept_name ORDER BY pass desc limit 0,#{size}")
    List<Map<String, Object>> selectExamHistogram(String time, Integer testId, Integer size);

    @Select("SELECT q.bank_name as bankName,CONCAT(convert(SUM(es.exam_results=1)*100/count(es.exam_results),decimal(10,2)),'') as pass," +
            "CONCAT(convert(SUM(es.exam_results!=1)*100/count(es.exam_results),decimal(10,2)),'') as nopass " +
            "from exam_info_summary as es,test_paper as t,question_bank as q " +
            "where es.test_paper_id=t.id and t.question_bank_id=q.id and es.test_paper_id in (${ids}) GROUP BY q.bank_name ORDER BY pass desc limit 0,#{size}")
    List<Map<String, Object>> scoreHistogram(Integer size,String ids);

    @Select("select COUNT(DISTINCT user_id) FROM exam_info_summary")
    Integer countPeople();

    @Select("SELECT sum(LENGTH(t.exam_crowd_ids)-LENGTH(REPLACE(t.exam_crowd_ids,',',''))) as num FROM test_paper as t")
    Double countNumber();

    @Select("SELECT COUNT(*) from exam_info_summary where exam_results=1")
    Double selectPassNum();

    @Select("SELECT DISTINCT dept_name as deptName, IFNULL( tb.num, 0) num " +
            "FROM user " +
            "LEFT JOIN (" +
            "select sum(es.exam_results=2) as num,user.dept_name as deptName from exam_info_summary as es,user where es.user_id=user.id group by user.dept_name ) AS tb " +
            "ON user.dept_name = tb.deptName ORDER BY num desc")
    List<Map<String, Object>> annularPie();

    @Select("SELECT count(es.user_id) as total,user.username,user.dept_name as deptName FROM exam_info_summary as es,user " +
            "WHERE es.user_id=user.id and DATE(es.end_time)>=#{starTime} AND DATE(es.end_time)<=#{endTime} GROUP BY user.username ORDER BY total desc limit 0,10")
    List<Map<String, Object>> numRank(String starTime, String endTime);

    @Select("select id,name from test_paper order by update_time desc")
    List<Map<String, Object>> getTestId();

    @Select("SELECT " +
            "IFNULL(bbb.percentage, 0) as percentage,aaa.date " +
            "FROM( " +
            "select date from " +
            "( " +
            "SELECT CONCAT('1月') AS date UNION " +
            "SELECT CONCAT('2月') AS date UNION " +
            "SELECT CONCAT('3月') AS date UNION " +
            "SELECT CONCAT('4月') AS date UNION " +
            "SELECT CONCAT('5月') AS date UNION " +
            "SELECT CONCAT('6月') AS date UNION " +
            "SELECT CONCAT('7月') AS date UNION " +
            "SELECT CONCAT('8月') AS date UNION " +
            "SELECT CONCAT('9月') AS date UNION " +
            "SELECT CONCAT('10月') AS date UNION " +
            "SELECT CONCAT('11月') AS date UNION " +
            "SELECT CONCAT('12月') AS date) as a " +
            ")aaa " +
            "LEFT JOIN( " +
            "SELECT MONTH(es.end_time) as date,CONCAT(convert(SUM(es.exam_results=1)*100/count(es.id),decimal(10,2)),'') as percentage " +
            "FROM exam_info_summary as es GROUP BY MONTH(es.end_time) ORDER BY date ) bbb on bbb.date = aaa.date")
    List<Map<String, Object>> passRate();

    @Select("SELECT SUM(es.exam_results!=1) as num,user.username,user.dept_name as deptName FROM exam_info_summary as es,user " +
            "WHERE es.user_id=user.id GROUP BY user.username ORDER BY num DESC limit 0,10")
    List<Map<String, Object>> noPassList();

    @Select("SELECT user.username,user.dept_name as deptName, ROUND(AVG(es.score),0) as avg,COUNT(es.id) as total FROM exam_info_summary as es,user " +
            "where es.user_id=user.id and DATE(es.end_time)>=#{starTime} AND DATE(es.end_time)<=#{endTime} GROUP BY user.username ORDER BY avg DESC limit 0,10")
    List<Map<String, Object>> scoreRate(String starTime, String endTime);

    @Select("SELECT DISTINCT dept_name as deptName from user ORDER BY dept_name desc limit 0,#{size}")
    List<Map<String, Object>> selectDeptName(Integer size);

    @Delete("DELETE from user_error_records where id=#{id}")
    void delWrongById(Integer id);

    @Delete("DELETE from user_error_records where user_id=#{userId}")
    void delWrongByUserId(Integer userId);

    @Select("select test_paper_id as testId,name from exam_info_summary order by update_time desc limit 0,1")
    Map<String, Object> getTestName();
}
