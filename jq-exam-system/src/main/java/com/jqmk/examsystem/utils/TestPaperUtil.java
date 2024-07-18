package com.jqmk.examsystem.utils;

import com.jqmk.examsystem.entity.TestPaper;

public class TestPaperUtil {
    public static boolean ifSumLegal(TestPaper testPaper){
        // 验证单选题、多选题和判断题的分数总和是否等于 100 分
        int totalScore = testPaper.getSingleChoiceScore() * testPaper.getSingleChoiceNum()
                + testPaper.getMultiChoiceScore() * testPaper.getMultiChoiceNum()
                + testPaper.getJudgeChoiceScore() * testPaper.getJudgeChoiceNum();
        if (totalScore != 100) {
            return false;
        }
        return true;
    }
}
