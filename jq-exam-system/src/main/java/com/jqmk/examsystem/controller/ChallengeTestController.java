package com.jqmk.examsystem.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.TestPaperChallengeDto;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.TestPaper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.service.TestPaperService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ChallengeTestController
 * @Author tian
 * @Date 2024/6/25 10:08
 * @Description 挑战答题列表界面
 */
@RestController
@RequestMapping("/challengeExam")
public class ChallengeTestController {

    @Resource
    private TestPaperService testPaperService;
    @Resource
    private TestPaperMapper testPaperMapper;

    @GetMapping("/main")
    public WebResult viewMain() {
        List<TestPaper> testPaperList = testPaperMapper.selectList(new QueryWrapper<TestPaper>()
                .select("id","name","pass_score","start_time","end_time","status","exam_introduce")
                .eq("no_challenge",1).orderByDesc("update_time"));
        return WebResult.ok().data(BeanUtil.copyToList(testPaperList, TestPaperChallengeDto.class));
    }

    @GetMapping("/viewExam")
    public WebResult viewExamById(Integer id) {
        TestPaper testPaper = testPaperMapper.selectById(id);
        return WebResult.ok().data(testPaper);
    }
    @PostMapping("/add")
    public WebResult addTestPaperRuler(@RequestBody TestPaper testPaper) {
        testPaper.setExamCategoryId(0);
        testPaper.setNoChallenge(1);
        testPaperService.save(testPaper);
        return WebResult.ok().message("创建考试规则成功");
    }

    @PostMapping("/update")
    public WebResult updateTestPaperRuler(@RequestBody TestPaper testPaper) {
        testPaper.setExamCategoryId(0);
        testPaper.setUpdateTime(LocalDateTime.now());
        testPaperService.updateById(testPaper);
        return WebResult.ok().message("更新成功");
    }

    @DeleteMapping("/delete")
    public WebResult deleteCategory(@RequestBody TestPaper testPaper) {
        testPaperService.removeById(testPaper);
        return WebResult.ok().message("删除成功");
    }

    @GetMapping("/select")
    public WebResult selectTestRuler(@RequestParam(required=false) String name,@RequestParam(required=false) Integer status,
                                     @RequestParam(required=false) LocalDateTime startTime,@RequestParam(required=false) LocalDateTime endTime) {
        List<TestPaper> testPapers = testPaperMapper.selectList(new QueryWrapper<TestPaper>()
                .select("id","name","pass_score","start_time","end_time","status","exam_introduce")
                .like(name != null, "name", name)
                .eq("no_challenge",1)
                .eq(status != null, "status", status)
                .between(startTime!=null,"end_time",startTime,endTime).orderByDesc("update_time"));
        return WebResult.ok().data(BeanUtil.copyToList(testPapers, TestPaperChallengeDto.class));
    }
}
