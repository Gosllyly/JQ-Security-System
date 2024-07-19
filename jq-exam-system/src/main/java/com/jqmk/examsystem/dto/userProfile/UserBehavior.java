package com.jqmk.examsystem.dto.userProfile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @ClassName UserBehavior
 * @Author tian
 * @Date 2024/7/19 9:54
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBehavior {

    /*检测时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime detectTime;

    /*头盔穿戴结果，插入时，1：已穿戴，2：代表穿戴位置错误，0：代表未穿戴，-1未检测*/
    private Integer helmetStatus;
    /*毛巾穿戴结果，插入时，1：已穿戴，2：代表穿戴位置错误，0：代表未穿戴，-1未检测*/
    private Integer towelStatus;
    /*自救器穿戴结果，插入时，1：已穿戴，2：代表穿戴位置错误，0：代表未穿戴，-1未检测*/
    private Integer rescuerStatus;
    /*胶鞋穿戴结果，插入时，1：已穿戴，2：代表穿戴位置错误，0：代表未穿戴，-1未检测*/
    private Integer shoesStatus;
    /*头灯穿戴结果，插入时，1：已穿戴，2：代表穿戴位置错误，0：代表未穿戴，-1未检测*/
    private Integer capLampStatus;
}