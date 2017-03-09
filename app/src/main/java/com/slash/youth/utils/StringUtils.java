package com.slash.youth.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    //判断是否是中英文
    public static boolean isSearchContent(String text) {
        String check = "^[\\u4E00-\\u9FA5A-Za-z]+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(text);

        return matcher.matches();
    }

    //纯数字 "[0-9]+"
    public static boolean isSearchNumberContent(String text) {
        String check = "[0-9]+";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(text);
        return matcher.matches();
    }

    public static String getLongToDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        return sd.format(date);
    }


}
