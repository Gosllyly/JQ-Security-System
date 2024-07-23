package com.jqmk.examsystem.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @ClassName QuestionTypeEnum
 * @Author tian
 * @Date 2024/7/23 9:42
 * @Description 问题类型枚举
 */
@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {

    /**
     * 未知
     */
    SINGLE(1, "单选题"),

    /**
     * 男性
     */
    MULTIPLE(2, "多选题"),

    /**
     * 女性
     */
    JUDGE(3, "判断题");

    private final Integer value;

    @JsonFormat
    private final String description;

    public static QuestionTypeEnum convert(Integer value) {
        return Stream.of(values())
                .filter(bean -> bean.value.equals(value))
                .findAny()
                .orElse(null);
    }

    public static QuestionTypeEnum convert(String description) {
        return Stream.of(values())
                .filter(bean -> bean.description.equals(description))
                .findAny()
                .orElse(null);
    }
}