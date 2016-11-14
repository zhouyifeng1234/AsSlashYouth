package com.slash.youth.engine;

import com.slash.youth.utils.LogKit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

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
            LogKit.v(senderUserId);

            return false;
        }
    }

}
