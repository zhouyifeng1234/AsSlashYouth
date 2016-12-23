package com.slash.youth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zss on 2016/11/24.
 */
public class PatternUtils {

    public static String letterRegex = "[a-zA-Z]";//字母
    public static String chineseRegex = "[\u4e00-\u9fa5]";//中文
    public static String numberRegex = "[0-9]*";//数字
    String pattern = "[^\u4e00-\u9fa5]";//非中文

    public static boolean match(String regex ,String text){
        Pattern p1 = Pattern.compile(regex);
        Matcher m = p1.matcher(text);
        return m.matches();
    }

}
