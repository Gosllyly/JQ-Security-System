package com.jqmk.examsystem.component;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.jqmk.examsystem.enums.ExamResultEnum;
import com.jqmk.examsystem.enums.QuestionTypeEnum;

/**
 * @ClassName ResultTypeConverter
 * @Author tian
 * @Date 2024/7/29 8:57
 * @Description 考试成绩结果类型转换
 */
public class ResultTypeConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) {
        return ExamResultEnum.convert(context.getReadCellData().getStringValue()).getValue();
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
        return new WriteCellData<>(ExamResultEnum.convert(context.getValue()).getDescription());
    }
}
