package com.slash.youth.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.slash.youth.BuildConfig;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.utils.ActivityUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import io.rong.imlib.RongIMClient;


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
        PlatformConfig.setWeixin(GlobalConstants.ThirdAppId.APPID_WECHAT, GlobalConstants.ThirdAppId.AppSecret_WECHAT);
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone(GlobalConstants.ThirdAppId.APPID_QQ, GlobalConstants.ThirdAppId.APPKEY_QQ);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogKit.v("onActivityCreated");
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogKit.v("onActivityStarted");
                ActivityUtils.currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogKit.v("onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogKit.v("onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogKit.v("onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogKit.v("onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogKit.v("onActivityDestroyed");
            }
        });

        UMShareAPI.get(this);
        Config.REDIRECT_URL = "www.slashyouth.com";

        context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();
        application = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
//        x.image().clearCacheFiles();
//        x.image().clearMemCache();
        //注册微信的APPID

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }

        LogKit.v("one");
        //注册融云消息接收监听器
        MsgManager.setMessReceiver();
        //通过token连接融云
        //10000的token
        MsgManager.connectRongCloud("wjTdt2YAMEl+kgwlIqEc9lpJh/sDSWvAD7s9SQ9t2IpAr12GGUolcoB06J/c93lRr1B3q/o8EjamBqhw9iQHqA==");

        //10002的token
//        MsgManager.connectRongCloud("OWellbpuoVve83MyzB06YVpJh/sDSWvAD7s9SQ9t2IpAr12GGUolcieJmtTlGLY0xSEqae+jIN8rMYEU2oRTbQ==");

        //10003的tokenF
//        MsgManager.connectRongCloud("J+NjV3RqnU1xfMolWAd6T1pJh/sDSWvAD7s9SQ9t2IpAr12GGUolclxn65zIYrLNwE/geOWQulCDALq54CXhkA==");
        LogKit.v("two");
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
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
