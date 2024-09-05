package com.jqmk.examsystem.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName strToJson
 * @Author tian
 * @Date 2024/6/19 9:24
 * @Description 拼接题库选项的字符串，并转换为json
 */
public class StringsUtil {

    public static JSONObject strToJson(String str) {
        String newStr = str.replace("、", "\":\"");
        String newStr1 = newStr.replace("\n", "\",\"");
        List<String> list = new ArrayList<>(Arrays.asList(newStr1));

        /**  用StringBuilder作字符串拼接  */
        StringBuilder sb = new StringBuilder();
        sb.append("{\"");
        int seq = 1;
        for (String s : list) {

            sb.append(s);
            if (seq++ != list.size()){
                sb.append("、");
            }
        }
        sb.append("\"}");
        JSONObject options = JSONObject.parseObject(String.valueOf(sb));
        return options;
    }
    public static String arrayToString(String str) {
        if (str!=null) {
            String newStr = str.replace(",", "|");
            String newStr1 = newStr.replace("[", "");
            String newStr2 = newStr1.replace("]", "");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String arrayToString1(List<String> str) {
        if (str!=null) {
            String newStr = str.toString().replace(",\n", "|");
            String newStr1 = newStr.replace("[", "");
            String newStr2 = newStr1.replace("]", "");
            String newStr3 = newStr2.replace("\"", "");
            String newStr4 = newStr3.replace(", ", "|");
            return newStr4;
        }else {
            return null;
        }
    }
    public static String arrayToStr(String str) {
        if (str!=null) {
            String newStr = str.replace(",\n", "\",\"");
            String newStr1 = newStr.replace("[", "\"");
            String newStr2 = newStr1.replace("]", "\"");
            String newStr3 = newStr2.replace(", ", "\",\"");
            return newStr3;
        }else {
            return null;
        }
    }
    public static String intToStr(String str) {
        if (str!=null) {
            String newStr = str.replace(",", "\",\"");
            List<String> list = new ArrayList<>(Arrays.asList(newStr));
            /**  用StringBuilder作字符串拼接  */
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            int seq = 1;
            for (String s : list) {

                sb.append(s);
                if (seq++ != list.size()){
                    sb.append("、");
                }
            }
            sb.append("\"");
            return String.valueOf(sb);
        }
        return null;
    }
    public static StringBuilder strAdd(String str) {
        if (str!=null) {
            String newStr = str.replace(",", "\",\"");
            List<String> list = new ArrayList<>(Arrays.asList(newStr));
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            int seq = 1;
            for (String s : list) {

                sb.append(s);
                if (seq++ != list.size()){
                    sb.append(",");
                }
            }
            sb.append("\"");
            return sb;
        }else {
            return null;
        }
    }
    public static String strTolist(String str) {
        if (str!=null) {
            String newStr = str.replace(", ", ",");
            String newStr1 = newStr.replace("[", "");
            String newStr2 = newStr1.replace("]", "");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String stringWipe(String str) {
        if (str!=null) {
            String newStr = str.replace("][", ", ");
            String newStr1 = newStr.replace("[", "");
            String newStr2 = newStr1.replace("]", "");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String stringRecom (String str) {
        if (str!=null) {
            String newStr = str.replace("[", "");
            String newStr1 = newStr.replace("]", "");
            return newStr1;
        }else {
            return null;
        }
    }
    public static String stringRecomByAn (String str) {
        if (str!=null) {
            String newStr = str.replace("[", "");
            String newStr1 = newStr.replace("]", "");
            String newStr2 = newStr1.replace(", ", ",");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String stringRecomNew (String str) {
        if (str!=null) {
            String newStr = str.replace("[", "");
            String newStr1 = newStr.replace("]", "");
            String newStr2 = newStr1.replace(" ", "");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String stringCut (String str) {
        if (str!=null) {
            String newStr = str.replaceAll("\n", "\",\"");
            List<String> list = new ArrayList<>(Arrays.asList(newStr));
            /**  用StringBuilder作字符串拼接  */
            StringBuilder sb = new StringBuilder();
            sb.append("[\"");
            int seq = 1;
            for (String s : list) {

                sb.append(s);
                if (seq++ != list.size()){
                    sb.append(",");
                }
            }
            sb.append("\"]");
            return String.valueOf(sb);
        }else {
            return null;
        }
    }
    public static String strToList (String str) {
        if (str!=null) {
            String newStr = str.replaceAll(",", "\",\"");
            List<String> list = new ArrayList<>(Arrays.asList(newStr));
            /**  用StringBuilder作字符串拼接  */
            StringBuilder sb = new StringBuilder();
            sb.append("[\"");
            int seq = 1;
            for (String s : list) {

                sb.append(s);
                if (seq++ != list.size()){
                    sb.append(",");
                }
            }
            sb.append("\"]");
            return String.valueOf(sb);
        }else {
            return null;
        }
    }
    public static String listWipe(String str) {
        if (str!=null) {
            String newStr = str.replace("[", "");
            String newStr1 = newStr.replace("]", "");
            return newStr1;
        }else {
            return null;
        }
    }
    public static String jsonToStr(String str) {
        if (str!=null) {
            String newStr = str.replace("{\"", "");
            String newStr1 = newStr.replace("\"}", "");
            String newStr2 = newStr1.replace("\"", "");
            String newStr3 = newStr2.replace(",", "\n");
            String newStr4 = newStr3.replaceAll(":", "、");
            return newStr4;
        }else {
            return null;
        }
    }
    public static String StingToStr(String str) {
        if (str!=null) {
            String newStr = str.replace("[", "");
            String newStr1 = newStr.replace("]", "");
            String newStr2 = newStr1.replace(", ", "\n");
            return newStr2;
        }else {
            return null;
        }
    }
    public static String removeWhitespace(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\s+", "");
    }
}
