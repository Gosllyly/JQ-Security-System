package com.jqmk.examsystem.component;

import com.alibaba.fastjson.JSON;
import com.jqmk.examsystem.config.MasInteractiveConfig;
import com.jqmk.examsystem.dto.mas.MasLoginRespDto;
import com.jqmk.examsystem.dto.mas.MasPerson;
import com.jqmk.examsystem.dto.mas.MasPersonListRespDto;
import com.jqmk.examsystem.dto.mas.MasPersonResBody;
import com.jqmk.examsystem.entity.User;
import com.jqmk.examsystem.mapper.UserMapper;
import com.jqmk.examsystem.utils.HttpClientUtil;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TimedTask
 * @Author tian
 * @Date 2024/7/11 8:26
 * @Description 定时任务
 */
@Component
@Slf4j
@EnableAsync
public class TimedTask {

    @Resource
    private MasInteractiveConfig masInteractiveConfig;
    @Resource
    private UserMapper userMapper;


    //@Scheduled(cron = "0 0/10 13 * * ?")
    public void syncEmployeeInfoMas() throws URISyntaxException {
        String loginAddress = masInteractiveConfig.getServiceAddress() + masInteractiveConfig.getLoginUrl();
        Map<String, Object> loginBody = new HashMap<>();
        log.info("开始从梅安森同步用户数据...");
        loginBody.put("UserName", masInteractiveConfig.getUsername());
        loginBody.put("Password", masInteractiveConfig.getPassword());
        String s = HttpClientUtil.httpPostRequest(loginAddress, loginBody);
        MasLoginRespDto masLoginRespDto = JSON.parseObject(s, MasLoginRespDto.class);
        if (masLoginRespDto.getCode() != 200) {
            log.error("梅安森系统登录失败......");
            return;
        }
        String token = masLoginRespDto.getData();
        String userListAddress = masInteractiveConfig.getServiceAddress() + masInteractiveConfig.getUserListUrl();
        // 分页获取员工数据，每页10条
        Integer curPageSize = Integer.MAX_VALUE, pageIndex = 1, pageSize = 50;
        while (curPageSize >= pageSize) {
            Map<String, Object> pagesInfo = new HashMap<>();
            //新的档案读取接口，要求把字段放入数组，rows是一页多少条，page是第几页，需要传入这两个字段，token放在header里
            pagesInfo.put("rows", pageSize);
            pagesInfo.put("page", pageIndex);
            Map<String, Object> headers = new HashMap<>();
            headers.put("token", token);
            String personStr = HttpClientUtil.httpGetRequest(userListAddress, headers, pagesInfo);
            MasPersonListRespDto masPersonListRespDto = JSON.parseObject(personStr, MasPersonListRespDto.class);

            if (masPersonListRespDto.getState() != 200) {
                log.error("从梅安森获取员工信息失败，原因：{}", masPersonListRespDto.getInfo());
                break;
            }
            MasPersonResBody respPersonBody = masPersonListRespDto.getData();
            curPageSize = respPersonBody.getRows().size();
            pageIndex++;

            // 同步员工数据到 mysql
            for (MasPerson person : respPersonBody.getRows()) {
                // 没有 idCard 的员工信息不同步
                if (person.getIDCard() == null || person.getIDCard().length() == 0)
                    continue;
                String imageUrl = masInteractiveConfig.getServiceAddress() + person.getImgFile();
                String image = person.getImgFile() == null ? null : HttpClientUtil.httpGetImage(imageUrl, new Pair<>("token", token));
                User user = User.builder()
                        .username(person.getName())
                        .idCard(person.getIDCard())
                        .deptName(person.getDeptName())
                        .jobType(person.getWorkName())
                        .cardNo(person.getCardNo())
                        .employeeId(person.getEmployee())
                        .imgFile(image)
                        .createDate(person.getUpdatedTime())
                        .build();
                User old = userMapper.selectByCardNoAndName(user.getIdCard(), user.getUsername());
                log.info(String.valueOf(old));
                // 新增或者更新
                if (old == null) {
                    userMapper.insertUser(user);
                } else if (!old.equals(user)) {
                    userMapper.updateUser(user.getUsername(), user.getDeptName(), user.getIdCard(), user.getCardNo(), user.getEmployeeId(), user.getJobType(),user.getImgFile(), user.getCreateDate());
                }
            }
            log.info("同步完成第 {} 页", pageIndex);
        }
        log.info("从梅安森同步用户数据成功...");
    }
}
