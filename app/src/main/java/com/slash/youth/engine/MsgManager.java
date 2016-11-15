package com.slash.youth.engine;

import com.slash.youth.utils.LogKit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MsgManager {

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connectRongCloud(String token) {

        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {

                LogKit.v("--onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {

                LogKit.v("--onSuccess---" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                LogKit.v("--onError" + errorCode);
            }
        });
    }

    public static void setMessReceiver() {
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
    }

    private static class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(Message message, int left) {
            //开发者根据自己需求自行处理
            String senderUserId = message.getSenderUserId();
            LogKit.v("senderUserId:" + senderUserId);

//            String extra = message.getExtra();
//            LogKit.v("extra:" + extra);

            String objectName = message.getObjectName();
            LogKit.v("objectName:" + objectName);

            if (objectName.equals("RC:TxtMsg")) {
                TextMessage textMessage = (TextMessage) message.getContent();
                String content = textMessage.getContent();
                String extra = textMessage.getExtra();
                LogKit.v("conent:" + content);
                LogKit.v("extra:" + extra);
            } else if (objectName.equals("RC:ImgMsg")) {
                ImageMessage imageMessage = (ImageMessage) message.getContent();

            } else if (objectName.equals("RC:VcMsg")) {
                VoiceMessage voiceMessage = (VoiceMessage) message.getContent();

            } else if (objectName.equals("RC:ImgTextMsg")) {


            } else if (objectName.equals("RC:LBSMsg")) {
                LocationMessage locationMessage = (LocationMessage) message.getContent();

            } else if (objectName.equals("RC:InfoNtf")) {

            } else if (objectName.equals("RC:ProfileNtf")) {

            } else if (objectName.equals("RC:CmdNtf")) {
                CommandNotificationMessage commandNotificationMessage = (CommandNotificationMessage) message.getContent();
                String name = commandNotificationMessage.getName();
                String data = commandNotificationMessage.getData();
                LogKit.v("CmdNtf name:" + name + "  data:" + data);

            } else if (objectName.equals("RC:CmdMsg")) {
                CommandMessage commandMessage = (CommandMessage) message.getContent();
                String name = commandMessage.getName();
                String data = commandMessage.getData();
                LogKit.v("CmdMsg name:" + name + " data:" + data);

            } else if (objectName.equals("RC:ContactNtf")) {

            } else {
                //这里可能是自定义的消息类型
            }


            return false;
        }
    }

}
