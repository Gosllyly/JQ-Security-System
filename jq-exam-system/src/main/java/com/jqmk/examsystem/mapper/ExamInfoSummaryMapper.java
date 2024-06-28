package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.dto.ExamLearnScore;
import com.jqmk.examsystem.dto.ExamLearnTime;
import com.jqmk.examsystem.dto.ExamRecordDto;
import com.jqmk.examsystem.entity.ExamInfoSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT `user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time FROM `user`,exam_info_summary as ex where (#{userId} IS NULL OR ex.user_id = #{userId}) limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectMain(Integer userId, Long page, Long pageSize);

    @Select("SELECT `user`.username,user.dept_name,user.job_type,ex.`name`,ex.start_time,ex.end_time FROM `user`,exam_info_summary as ex where (#{userId} IS NULL OR ex.user_id = #{userId}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR end_time >= #{startTime} AND end_time <= #{endTime} ) AND (#{examCategoryId} IS NULL OR exam_category_id = #{examCategoryId}) " +
            "AND (#{name} IS NULL OR name = #{name}) AND (#{examResults} IS NULL OR exam_results = #{examResults}) AND (#{deptName} IS NULL OR dept_name = #{deptName}) AND (#{jobType} IS NULL OR job_type = #{jobType}) " +
            "AND (#{username} IS NULL OR username = #{username}) limit ${page}, ${pageSize} ")
    List<ExamRecordDto> selectCondition(Integer userId, LocalDateTime startTime, LocalDateTime endTime, Integer examCategoryId, String name, Integer examResults, String deptName, String jobType, String username, Long page, Long pageSize);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,SUM(test_paper.learning_score) as credits ,ex.end_time " +
            "FROM `user`,exam_info_summary as ex,test_paper where ex.user_id =user.id GROUP BY user.username,ex.`name` order by end_time desc  limit ${page},${pageSize} ")
    List<ExamLearnScore> viewInterface(Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where ex.user_id =user.id GROUP BY user.username,ex.`name` limit 1")
    Integer viewInterfaceCount();

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_score,SUM(test_paper.learning_score) as credits,ex.end_time FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> selectLearnScore(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) ")
    Integer selectLearnScoreCount(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,sum(ex.obtain_learning_score) as obtainLearningScore,SUM(test_paper.learning_score) as credits,ex.end_time FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnScore> learnScoreCondition(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime, Long page, Long pageSize);
    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{examCategoryId} IS NULL OR ex.exam_category_id = #{examCategoryId}) AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) " +
            "AND ((#{startTime} and #{endTime}) IS NULL OR ex.end_time >= #{startTime} AND ex.end_time <= #{endTime}) GROUP BY user.username,ex.`name` limit 1 ")
    Integer learnScoreConditionCount(String deptName, Integer examCategoryId, String username, String name, String startTime, String endTime);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time " +
            "FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` order by end_time desc limit ${page},${pageSize} ")
    List<ExamLearnTime> learnTimeInterface(Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where  ex.user_id =user.id and test_paper.`name` = ex.`name` ")
    Integer learnTimeInterfaceCount();

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,ex.obtain_learning_time,ex.end_time,test_paper.learning_time  FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> selectLearnTime(String deptName, String username, String name, Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id ")
    Integer selectLearnTimeCount(String deptName, String username, String name);

    @Select("SELECT `user`.username,user.dept_name,ex.`name`,SUM(ex.obtain_learning_time) as obtainLearningTime,ex.end_time,SUM(test_paper.learning_time) as learningTime FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id GROUP BY user.id order by ex.end_time desc limit ${page}, ${pageSize} ")
    List<ExamLearnTime> learnTimeCondition(String deptName, String username, String name, Long page, Long pageSize);

    @Select("SELECT count(*) FROM `user`,exam_info_summary as ex,test_paper where (#{deptName} IS NULL OR user.dept_name = #{deptName}) " +
            "AND (#{username} IS NULL OR user.username = #{username}) AND (#{name} IS NULL OR ex.name = #{name}) and ex.user_id =user.id and test_paper.id = ex.test_paper_id ")
    Integer learnTimeConditionCount(String deptName, String username, String name);

}
