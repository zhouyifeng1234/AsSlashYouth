package com.slash.youth.ui.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.slash.youth.R;
import com.slash.youth.ui.activity.SplashActivity;
import com.slash.youth.utils.StringUtils;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by acer on 2017/3/2.
 */

public class SealNotificationReceiver extends PushMessageReceiver {

    /* push 通知到达事件*/
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        simpleNotify(context, message);
        return true; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    /* push 通知点击事件 */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }

    private void simpleNotify(Context context, PushNotificationMessage message) {
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String[] messages = new String[]{};
        if (!StringUtils.isEmpty(message.getPushContent())) {
            messages = message.getPushContent().split(":");
        }
        //Ticker是状态栏显示的提示
        if (messages.length >= 2) {
            builder.setTicker("您的好友" + messages[0] + "给您发了一条消息");
            //第一行内容  通常作为通知栏标题
            builder.setContentTitle(StringUtils.isEmpty(messages[0]) ? messages[0] : "");
            //第二行内容 通常是通知正文
            builder.setContentText(StringUtils.isEmpty(messages[1]) ? messages[1] : "");
        } else {
            builder.setTicker("消息提示");
            //第一行内容  通常作为通知栏标题
            builder.setContentTitle("");
            //第二行内容 通常是通知正文
            builder.setContentText("");
        }
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
//        builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        builder.setNumber(2);
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //下拉显示的大图标
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        //通知默认的声音 震动 呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();

        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manger.notify(1, notification);
    }
}
