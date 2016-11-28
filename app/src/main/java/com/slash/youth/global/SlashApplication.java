package com.slash.youth.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.slash.youth.engine.MsgManager;
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
       // MsgManager.connectRongCloud("wjTdt2YAMEl+kgwlIqEc9lpJh/sDSWvAD7s9SQ9t2IpAr12GGUolcoB06J/c93lRr1B3q/o8EjamBqhw9iQHqA==");
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
