package com.jqmk.examsystem.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @ClassName ExamResultEnum
 * @Author tian
 * @Date 2024/7/29 8:52
 * @Description 考试成绩枚举
 */
@Getter
@AllArgsConstructor
public enum ExamResultEnum {
    /**
     * 及格
     */
    PASS(1, "及格"),

    /**
     * 不及格
     */
    FAIL(2, "不及格"),

    /**
     * 未参考
     */
    NOTREFER(0, "未参考");

    private final Integer value;

    @JsonFormat
    private final String description;

    public static ExamResultEnum convert(Integer value) {
        return Stream.of(values())
                .filter(bean -> bean.value.equals(value))
                .findAny()
                .orElse(null);
    }

    public static ExamResultEnum convert(String description) {
        return Stream.of(values())
                .filter(bean -> bean.description.equals(description))
                .findAny()
                .orElse(null);
    }
}
