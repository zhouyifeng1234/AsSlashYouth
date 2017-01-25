package com.slash.youth.utils;

import com.google.gson.Gson;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.engine.MsgManager;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by zhouyifeng on 2017/1/23.
 */
public class ShareTaskUtils {

    public static void sendShareTask(final ChatCmdShareTaskBean chatCmdShareTaskBean, final String targetId) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(chatCmdShareTaskBean);
        TextMessage textMessage = TextMessage.obtain(MsgManager.CHAT_CMD_SHARE_TASK);
        textMessage.setExtra(jsonData);

        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, textMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                MsgManager.updateConversationList(targetId);//更新会话列表
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        });
    }
}
