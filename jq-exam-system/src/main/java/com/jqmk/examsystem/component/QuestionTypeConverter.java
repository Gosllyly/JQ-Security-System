package com.jqmk.examsystem.component;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.jqmk.examsystem.enums.QuestionTypeEnum;

/**
 * @ClassName TypeConverter
 * @Author tian
 * @Date 2024/7/23 9:39
 * @Description 问题类型转换
 */
public class QuestionTypeConverter implements Converter<Integer> {
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
        return QuestionTypeEnum.convert(context.getReadCellData().getStringValue()).getValue();
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
        return new WriteCellData<>(QuestionTypeEnum.convert(context.getValue()).getDescription());
    }
}