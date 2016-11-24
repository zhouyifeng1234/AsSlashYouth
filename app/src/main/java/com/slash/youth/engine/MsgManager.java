package com.slash.youth.engine;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemPushInfoBinding;
import com.slash.youth.domain.PushInfoBean;
import com.slash.youth.ui.viewmodel.ItemPushInfoModel;
import com.slash.youth.utils.ActivityUtils;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MsgManager {

    public static final String CHAT_CMD_ADD_FRIEND = "addFriend";
    public static final String CHAT_CMD_SHARE_TASK = "shareTask";
    public static final String CHAT_CMD_BUSINESS_CARD = "businessCard";
    public static final String CHAT_CMD_CHANGE_CONTACT = "changeContact";

    private static ChatTextListener mChatTextListener;
    private static ChatPicListener mChatPicListener;
    private static ChatVoiceListener mChatVoiceListener;
    private static ChatOtherCmdListener mChatOtherCmdListener;

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connectRongCloud(String token) {

        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                String connMessage = connectionStatus.getMessage();
                LogKit.v("connectionStatus:" + connMessage);
            }
        });
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

    private static class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(final Message message, final int left) {

            //开发者根据自己需求自行处理
            final String senderUserId = message.getSenderUserId();
            LogKit.v("senderUserId:" + senderUserId);

            final String objectName = message.getObjectName();
            LogKit.v("objectName:" + objectName);

//            if (objectName.equals("RC:TxtMsg")) {
//                TextMessage textMessage = (TextMessage) message.getContent();
//                String content = textMessage.getContent();
//                String extra = textMessage.getExtra();
//                LogKit.v("conent:" + content);
//                LogKit.v("extra:" + extra);
//            } else if (objectName.equals("RC:ImgMsg")) {
//                ImageMessage imageMessage = (ImageMessage) message.getContent();
//
//            } else if (objectName.equals("RC:VcMsg")) {
//                VoiceMessage voiceMessage = (VoiceMessage) message.getContent();
//
//            } else if (objectName.equals("RC:ImgTextMsg")) {
//
//
//            } else if (objectName.equals("RC:LBSMsg")) {
//                LocationMessage locationMessage = (LocationMessage) message.getContent();
//
//            } else if (objectName.equals("RC:InfoNtf")) {
//
//            } else if (objectName.equals("RC:ProfileNtf")) {
//
//            } else if (objectName.equals("RC:CmdNtf")) {
//                CommandNotificationMessage commandNotificationMessage = (CommandNotificationMessage) message.getContent();
//                String name = commandNotificationMessage.getName();
//                String data = commandNotificationMessage.getData();
//                LogKit.v("CmdNtf name:" + name + "  data:" + data);
//
//            } else if (objectName.equals("RC:CmdMsg")) {
//                CommandMessage commandMessage = (CommandMessage) message.getContent();
//                String name = commandMessage.getName();
//                String data = commandMessage.getData();
//                LogKit.v("CmdMsg name:" + name + " data:" + data);
//
//            } else if (objectName.equals("RC:ContactNtf")) {
//
//            } else {
//                //这里可能是自定义的消息类型
//            }

            if (senderUserId == "100") {//系统推送账号

            } else if (senderUserId == "1000") {//斜杠小助手

            } else {//聊天消息
                if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {//判断是单聊消息
                    //接收聊天的文本消息
                    if (objectName.equals("RC:TxtMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mChatTextListener != null && targetId.equals(senderUserId)) {
                                    mChatTextListener.displayText(message, left);
                                } else {
                                    //消息推送的顶部弹框提示
                                    TextMessage textMessage = (TextMessage) message.getContent();
                                    PushInfoBean pushInfoBean = new PushInfoBean();
                                    pushInfoBean.pushText = textMessage.getContent();
                                    displayPushInfo(pushInfoBean);
                                }
                            }
                        });
                    }
                    //接收聊天的图片消息
                    else if (objectName.equals("RC:ImgMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mChatPicListener != null && targetId.equals(senderUserId)) {
                                    mChatPicListener.dispayPic(message, left);
                                } else {
                                    //消息推送的顶部弹框提示
                                }
                            }
                        });
                    }
                    //接受聊天的语音消息
                    else if (objectName.equals("RC:VcMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mChatVoiceListener != null && targetId.equals(senderUserId)) {
                                    mChatVoiceListener.loadVoice(message, left);
                                } else {
                                    //消息推送的顶部弹框提示
                                }
                            }
                        });
                    }
                    //接收聊天中的其它命令消息
                    else if (objectName.equals("RC:CmdMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mChatVoiceListener != null && targetId.equals(senderUserId)) {
                                    mChatOtherCmdListener.doOtherCmd(message, left);
                                } else {
                                    //消息推送的顶部弹框提示
                                }
                            }
                        });
                    }
                }
            }

            return false;
        }
    }

    public static void displayPushInfo(PushInfoBean pushInfoBean) {
        Activity topActivity = ActivityUtils.currentActivity;
        ItemPushInfoBinding itemPushInfoBinding =
                DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_push_info, null, false);
        ItemPushInfoModel itemPushInfoModel = new ItemPushInfoModel(itemPushInfoBinding, topActivity, pushInfoBean);
        itemPushInfoBinding.setItemPushInfoModel(itemPushInfoModel);
        final View pushView = itemPushInfoBinding.getRoot();
        pushView.measure(0, 0);
        final int measuredHeight = pushView.getMeasuredHeight();
        TranslateAnimation translateAppearAnimation = createPushInfoAppearAnimation(measuredHeight);
        translateAppearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //消失动画
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation translateHideAnimation = createPushInfoHideAnimation(measuredHeight);
                        translateHideAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //执行完隐藏动画后，把View从 Activity中移除，释放系统资源
                                LogKit.v("移除pushView,释放资源");
                                ViewGroup viewGroup = (ViewGroup) pushView.getParent();
                                viewGroup.removeView(pushView);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        pushView.startAnimation(translateHideAnimation);
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topActivity.addContentView(pushView, new FrameLayout.LayoutParams(-1, -2));
        //出现动画
        pushView.startAnimation(translateAppearAnimation);
    }

    public static TranslateAnimation createPushInfoAppearAnimation(int measuredHeight) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -measuredHeight, 0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public static TranslateAnimation createPushInfoHideAnimation(int measuredHeight) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -measuredHeight);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public static void loadHistoryChatRecord() {
        getHisMsgFromLocal();
//        getHisMsgFromRemote();
    }

    /**
     * 从客户端本地读取聊天记录
     */
    private static void getHisMsgFromLocal() {
        RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, 20, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                LogKit.v("Message Count:" + messages.size());
                for (Message message : messages) {
                    LogKit.v("SenderId:" + message.getSenderUserId() + "  sentTime:" + message.getSentTime() + "   Direction:" + message.getMessageDirection() + " objectName:" + message.getObjectName());
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 从融云服务器远程获取聊天历史记录
     */
    private static void getHisMsgFromRemote() {
        /**
         * 根据会话类型的目标 Id，回调方式获取某消息类型标识的N条历史消息记录。
         *
         * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM 聊天室会话类型。
         * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 。
         * @param dateTime         从该时间点开始获取消息。即：消息中的 sentTime；第一次可传 0，获取最新 count 条。
         * @param count            要获取的消息数量，最多 20 条。
         * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
         */
        RongIMClient.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, SystemClock.currentThreadTimeMillis() - 60 * 60 * 1000, 5, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                LogKit.v("get Remote His ----------------------------------");
                LogKit.v("get Remote His " + messages);
                LogKit.v("Reomte Message Count:" + messages.size());
                for (Message message : messages) {
                    LogKit.v("Reomte------SenderId:" + message.getSenderUserId() + "  sentTime:" + message.getSentTime() + "   Direction:" + message.getMessageDirection() + " objectName:" + message.getObjectName());
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogKit.v("Remote------errorCode:" + errorCode);
            }
        });

    }

    //自定义的各种聊天消息的监听器

    public static String targetId = "-1";

    public interface ChatTextListener {
        public void displayText(Message message, int left);
    }

    public interface ChatPicListener {
        public void dispayPic(Message message, int left);
    }

    public interface ChatVoiceListener {
        public void loadVoice(Message message, int left);
    }

    public interface ChatOtherCmdListener {
        public void doOtherCmd(Message message, int left);
    }

    public static void setMessReceiver() {
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
    }

    public static void setChatTextListener(ChatTextListener chatTextListener) {
        mChatTextListener = chatTextListener;
    }

    public static void removeChatTextListener() {
        mChatTextListener = null;
    }

    public static void setChatPicListener(ChatPicListener chatPicListener) {
        mChatPicListener = chatPicListener;
    }

    public static void removeChatPicListener() {
        mChatPicListener = null;
    }

    public static void setChatVoiceListener(ChatVoiceListener chatVoiceListener) {
        mChatVoiceListener = chatVoiceListener;
    }

    public static void removeChatVoiceListener() {
        mChatVoiceListener = null;
    }

    public static void setChatOtherCmdListener(ChatOtherCmdListener chatOtherCmdListener) {
        mChatOtherCmdListener = chatOtherCmdListener;
    }

    public static void removeChatOtherCmdListener() {
        mChatOtherCmdListener = null;
    }
}
