package com.jqmk.examsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jqmk.examsystem.dto.WebResult;
import com.jqmk.examsystem.entity.TestPaper;
import com.jqmk.examsystem.mapper.TestPaperMapper;
import com.jqmk.examsystem.service.TestPaperService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
        return WebResult.ok().data(testPaperMapper.selectMaps(new QueryWrapper<TestPaper>()
                .select("id","name","pass_score","start_time","end_time","status","exam_introduce")
                .eq("no_challenge",1).orderByDesc("update_time")));
    }


    @PostMapping("/add")
    public WebResult addTestPaperRuler(@RequestBody TestPaper testPaper) {
        testPaper.setNoChallenge(1);
        testPaperService.save(testPaper);
        return WebResult.ok().message("创建考试规则成功");
    }

    @PostMapping("/update")
    public WebResult updateTestPaperRuler(@RequestBody TestPaper testPaper) {
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
        return WebResult.ok().data(testPaperMapper.selectMaps(new QueryWrapper<TestPaper>()
                .select("id","name","pass_score","start_time","end_time","status","exam_introduce")
                .like(name != null, "name", name)
                .eq("no_challenge",1)
                .eq(status != null, "status", status)
                .between(startTime!=null,"end_time",startTime,endTime).orderByDesc("update_time")));
    }
}
