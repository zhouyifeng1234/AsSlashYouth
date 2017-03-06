package com.slash.youth.utils;

import android.annotation.TargetApi;
import android.content.Context;

/**
 * Created by zss on 2016/12/13.
 */
public class CountUtils {
    public static String  DecimalFormat(double count){
        if(count!=0) {
            double v = (Math.round(count * 100) / 100.0);
            return String.valueOf(v);
        }

      /*  if(count!=0){
            String format = new DecimalFormat("#.00").format(count);
            return format;
        }*/
        return String.valueOf(count);
    }
}
