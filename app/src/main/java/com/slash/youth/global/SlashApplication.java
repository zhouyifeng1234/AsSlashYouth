package com.slash.youth.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class SlashApplication extends Application{
    private static Context context;
    private static int mainThreadId;
    private static Handler handler;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();
    }

    public static Context getContext() {
        return context;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }
}
