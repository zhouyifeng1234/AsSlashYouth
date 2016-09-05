package com.slash.youth.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class SlashApplication extends Application {
    private static Context context;
    private static int mainThreadId;
    private static Handler handler;
    private static Application application;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();
        application = this;
        x.Ext.init(this);
        //注册微信的APPID
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

    public static Application getApplication() {
        return application;
    }
}
