package com.slash.youth.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.DecimalFormat;

/**
 * Created by zss on 2016/12/13.
 */
public class CountUtils {

    @TargetApi(24)
    public static String  DecimalFormat(double count){

        if(count!=0){
            return new DecimalFormat(".00").format(count);
        }
        return String.valueOf(count);
    }

}
