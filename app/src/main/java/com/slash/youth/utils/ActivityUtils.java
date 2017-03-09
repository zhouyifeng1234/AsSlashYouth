package com.slash.youth.utils;

import android.app.Activity;

import com.slash.youth.ui.activity.base.BaseActivity;

/**
 * Created by zhouyifeng on 2016/11/22.
 */
public class ActivityUtils {

    public static Activity currentActivity;//用于获取当前顶部显示的Activity


    //这个方法好像行不通
//    public Activity getTopActivity() {
//        ActivityManager am = (ActivityManager) CommonUtils.getContext().getSystemService(CommonUtils.getContext().ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//
//    }
}
