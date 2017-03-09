package com.slash.youth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer on 2016/12/5.
 */
public class TimeUtils {
    private  static  long oneHour = 60*60*1000;  //一个小时
    private  static long oneDay = 24*oneHour;  //一天
    private static long currentTime;
    private static String pattern1 = "mm分钟";
    private static String pattern2 = "HH:mm";
    private static String pattern3 = "MM月dd日HH:mm";
    private static String pattern4 = "MM/dd/yyyy HH:mm:ss";
    private static String pattern5 = "yyyy年MM月dd日 HH:mm";


    public static String getTime(long uts) {
        currentTime = System.currentTimeMillis();

        if((currentTime-uts)<oneHour){
            return getString(uts,pattern1);
        }

        if((currentTime-uts)>oneHour&&(uts-currentTime)<oneDay){
            return getString(uts,pattern2);
        }

        if((currentTime-uts)>oneDay){
            return getString(uts,pattern3);
        }

        return"";
    }


    public static String getData(long uts) {
        return getString(uts,pattern3);
    }

    public static String getTimeData(long uts) {
        return getString(uts,pattern4);
    }

    private static String getString(long uts,String pattern) {
        SimpleDateFormat sdf= new SimpleDateFormat(pattern);
        Date date= new Date(uts);
        return sdf.format(date);
    }

    public static String getCurrentTime(){
        currentTime = System.currentTimeMillis();
        return getString(currentTime,pattern5);
    }

}
