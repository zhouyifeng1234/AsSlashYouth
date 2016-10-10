package com.slash.youth.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class SlashApplication extends Application {
    private static Context context;
    private static int mainThreadId;
    private static Handler handler;
    private static Application application;

    static {
        //友盟社会化组件三方平台相关AppKey
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        Config.REDIRECT_URL = "www.slashyouth.com";

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
