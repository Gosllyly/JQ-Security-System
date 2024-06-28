package com.jqmk.examsystem.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PageDto
 * @Author tian
 * @Date 2024/6/19 11:25
 * @Description
 */
@Data
public class PageDto<T> {

    private Long total;

    private Long pages;

    private List<T> list;
}
