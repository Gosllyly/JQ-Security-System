package com.jqmk.examsystem.dto.userProfile;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;

/**
 * @ClassName ResultSort
 * @Author tian
 * @Date 2024/8/16 9:30
 * @Description 进行结果排序需要的实体类
 */
@Data
public class ResultSort {

    private Long id;

    private String username;

    private String employeeId;
}
