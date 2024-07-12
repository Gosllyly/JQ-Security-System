package com.jqmk.examsystem.dto.mas;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName MasPerson
 * @Author tian
 * @Date 2024/7/11 8:33
 * @Description
 */
@Data
public class MasPerson {
    /*员工姓名*/
    private String Name;
    /*性别 男或女*/
    private String GenderName;
    /*部门名称*/
    private String DeptName;
    /*职务名称*/
    private String TitleName;
    /*工种名称*/
    private String WorkName;
    /*当前井下位置*/
    private String Wz;
    /*职称名*/
    private String ProfessorName;
    /*图片流文件*/
    private String ImgFile;
    /*人员考勤规则*/
    private String AttenRuleName;
    /*图片ID，新增修改时候用*/
    private String PhotoId;
    /*籍贯名称*/
    private String NativeName;
    /*班中餐名称*/
    private String LunchName;
    /*生日格式化*/
    private String BirthDateFormat;
    /*入职时间格式化*/
    private String EntryTimeFormat;
    /*唯一编码*/
    private String Id;
    /*唯一性识别装置标识*/
    private String UniqueRecognizeFlag;
    /*卡号*/
    private Integer CardNo;
    /*记录有效标志 0-删除 1-未删除*/
    private Integer IsDelete;
    /*灯架号*/
    private String LightSupport;
    /*自救器号*/
    private String Rescuer;
    /*便携仪号*/
    private String Portable;
    /*职务code*/
    private Integer TitleCode;
    /*工种code*/
    private Integer WorkCode;
    /*职称coe*/
    private String ProfessorCode;
    /*军区专用，衔级*/
    private String Rank;
    /*部门code*/
    private String DeptCode;
    /*工号*/
    private String Employee;
    /*考勤规则code*/
    private Integer AttenRuleCode;
    /*班中餐规则code*/
    private Integer LunchCode;
    /*0.否1.是*/
    private Integer SpecialWork;
    /*0.否1.是*/
    private Integer SubstituteLeader;
    /*岗位*/
    private Integer Quarter;
    /*干部标志 1-是 0-否*/
    private Integer IsCadre;
    /*婚姻标志 0-未婚 1-已婚*/
    private Integer Mary;
    /*出生日期*/
    private LocalDateTime BirthDate;
    /*民族*/
    private String Nation;
    /*政治面貌*/
    private String Political;
    /*身份证号*/
    private String IDCard;
    /*籍贯*/
    private Integer Native;
    /*社保号,加唯一性验证*/
    private String SocialSecNumber;
    /*家庭地址*/
    private String Address;
    /*学历*/
    private String Education;
    /*入职时间*/
    private LocalDateTime EntryTime;
    /*在职状态 0-离职 1-在职 2-试用*/
    private Integer Incumbency;
    /*联系电话*/
    private String Tel;
    /*行政级别(如：矿级，科级等)*/
    private Integer GovLevel;
    /*性别0=男，1=女*/
    private Integer Gender;
    /*DateTime*/
    private LocalDateTime UpdatedTime;
}