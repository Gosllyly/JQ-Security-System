package com.jqmk.examsystem.mapper;

import com.jqmk.examsystem.entity.QuestionCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 收藏题目，在收藏界面查看题目所属题库，以及题目的详细信息 Mapper 接口
 * </p>
 *
 * @author tian
 * @since 2024-06-17
 */
@Mapper
public interface QuestionCollectionMapper extends BaseMapper<QuestionCollection> {

    @Select("INSERT INTO question_collection(user_id,question_id,question_bank_name,type,stem,options,correct_options,analysis) " +
            "SELECT #{userId},q.id,qb.bank_name,q.type,q.stem,q.options,q.correct_options,q.analysis FROM question as q,question_bank as qb WHERE q.id=#{questionId} and q.question_bank_id=qb.id")
    void addCollection(Integer userId, Long questionId);
}
