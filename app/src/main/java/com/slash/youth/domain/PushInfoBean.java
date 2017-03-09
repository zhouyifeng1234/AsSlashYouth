package com.slash.youth.domain;

/**
 * Created by zhouyifeng on 2016/11/22.
 */
public class PushInfoBean {
    public static final int CHAT_TEXT_MSG = 0;//聊天中文本消息
    public static final int CHAT_IMG_MSG = 1;//聊天中图片消息
    public static final int CHAT_VOICE_MSG = 2;//聊天中语音消息
    public static final int CHAT_OTHER_TEXT_CMD_MSG = 3;//聊天中其它命令消息，添加好友、交换联系方式、分享任务、分享名片、同意添加好友、拒绝添加好友、同意交换联系方式、拒绝交换联系方式


    public String senderUserId;
    public String pushText;
    public int msg_type;
}
