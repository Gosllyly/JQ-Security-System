package com.jqmk.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jqmk.examsystem.entity.TestPaper;
import org.apache.ibatis.annotations.Delete;
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

    @Select("SELECT COUNT(*) FROM test_paper_question WHERE test_paper_id = #{id}")
    Integer selectCountTest(Integer id);

    @Select("select learning_score from test_paper where learning_score_recorded=1 and id = #{id}")
    Integer selectLearningScore(Integer id);

    @Select("select learning_time from test_paper where learning_time_recorded=1 and id = #{id}")
    Integer selectLearningTime(Integer id);
    @Select("SELECT CASE WHEN SUM(exam_info_summary.redo_num) > test_paper.redo_num THEN '1' ELSE '0'  END AS result " +
            "FROM exam_info_summary,test_paper WHERE exam_info_summary.user_id = 1 and exam_info_summary.test_paper_id=1 and test_paper.id =1 and test_paper.redo_num!=-1")
    Integer countRedoNum(Integer id,Integer userId);

    @Select("SELECT SUM(redo_num) FROM test_paper WHERE id = #{id}")
    Integer selectRedoNum(Integer id);

    @Select("select question_bank_id from test_paper WHERE id = #{id}")
    Integer selectOldId(Long id);

    @Delete("delete from FROM test_paper_question WHERE test_paper_id = #{id}")
    void delTestPaperBank(Long id);
}
