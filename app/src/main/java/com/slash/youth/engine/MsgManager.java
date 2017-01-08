package com.slash.youth.engine;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemPushInfoBinding;
import com.slash.youth.domain.PushInfoBean;
import com.slash.youth.domain.RongTokenBean;
import com.slash.youth.http.protocol.AddFriendProtocol;
import com.slash.youth.http.protocol.AddFriendStatusProtocol;
import com.slash.youth.http.protocol.AgreeAddFriendProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.ConversationListProtocol;
import com.slash.youth.http.protocol.GetIsChangeContactProtocol;
import com.slash.youth.http.protocol.RefreshRongTokenProtocol;
import com.slash.youth.http.protocol.RejectAddFriendProtocol;
import com.slash.youth.http.protocol.RongTokenProtocol;
import com.slash.youth.http.protocol.SetChangeContactProtocol;
import com.slash.youth.ui.viewmodel.ItemPushInfoModel;
import com.slash.youth.utils.ActivityUtils;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MsgManager {

    public static final String CHAT_CMD_ADD_FRIEND = "addFriend";
    public static final String CHAT_CMD_SHARE_TASK = "shareTask";
    public static final String CHAT_CMD_BUSINESS_CARD = "businessCard";
    public static final String CHAT_CMD_CHANGE_CONTACT = "changeContact";
    public static final String CHAT_CMD_AGREE_ADD_FRIEND = "agreeAddFriend";
    public static final String CHAT_CMD_REFUSE_ADD_FRIEND = "refuseAddFriend";
    public static final String CHAT_CMD_AGREE_CHANGE_CONTACT = "agreeChangeContact";
    public static final String CHAT_CMD_REFUSE_CHANGE_CONTACT = "refuseChangeContact";

    public static final String CHAT_TASK_INFO = "taskInfo";

    private static ChatTextListener mChatTextListener;
    private static ChatPicListener mChatPicListener;
    private static ChatVoiceListener mChatVoiceListener;
    private static ChatOtherCmdListener mChatOtherCmdListener;
    private static HistoryListener mHistoryListener;
    private static RelatedTaskListener mRelatedTaskListener;

    private static int getRongTokenTimes = 0;

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
                if (connMessage.equals("Login on the other device, and be kicked offline.")) {
                    ToastUtils.shortToast(connMessage);
                }
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
                //token如果无效，重新调用获取rongToken接口
                getRongTokenTimes++;
                if (getRongTokenTimes > 3) {//如果超过三次还失败，就提示“链接融云失败”
                    Looper.prepare();
                    ToastUtils.shortToast("链接融云失败");
                    Looper.loop();
                    return;
                }
                refreshRongToken(new BaseProtocol.IResultExecutor<RongTokenBean>() {
                    @Override
                    public void execute(RongTokenBean dataBean) {
                        String rongToken = dataBean.data.token;
                        connectRongCloud(rongToken);
                    }

                    @Override
                    public void executeResultError(String result) {
                        ToastUtils.shortToast("刷新融云token失败:" + result);
                    }
                }, LoginManager.currentLoginUserId + "", "111");//暂时phone参数随便传
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
            LogKit.v("left:" + left);

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
                    if (objectName.equals("RC:TxtMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                TextMessage textMessage = (TextMessage) message.getContent();
                                String extra = textMessage.getExtra();
                                //接收聊天的文本消息
                                if (TextUtils.isEmpty(extra)) {
                                    if (mChatTextListener != null && targetId.equals(senderUserId)) {
                                        mChatTextListener.displayText(message, left);
                                    } else {
                                        //消息推送的顶部弹框提示
                                        PushInfoBean pushInfoBean = new PushInfoBean();
                                        pushInfoBean.senderUserId = senderUserId;
                                        pushInfoBean.pushText = textMessage.getContent();
                                        pushInfoBean.msg_type = PushInfoBean.CHAT_TEXT_MSG;
                                        displayPushInfo(pushInfoBean);
                                    }
                                } else {
                                    //接收聊天中的其它命令消息
                                    if (mChatVoiceListener != null && targetId.equals(senderUserId)) {
                                        mChatOtherCmdListener.doOtherCmd(message, left);
                                    } else {
                                        //消息推送的顶部弹框提示
                                        PushInfoBean pushInfoBean = new PushInfoBean();
                                        pushInfoBean.senderUserId = senderUserId;
                                        String content = textMessage.getContent();
                                        if (content.contentEquals(MsgManager.CHAT_CMD_ADD_FRIEND)) {
                                            pushInfoBean.pushText = "请求添加您为好友";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_SHARE_TASK)) {
                                            pushInfoBean.pushText = "分享了一个任务";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_BUSINESS_CARD)) {
                                            pushInfoBean.pushText = "分享了个人名片";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_CHANGE_CONTACT)) {
                                            pushInfoBean.pushText = "请求交换联系方式";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_ADD_FRIEND)) {
                                            pushInfoBean.pushText = "同意添加您为好友";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_ADD_FRIEND)) {
                                            pushInfoBean.pushText = "拒绝添加您为好友";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_CHANGE_CONTACT)) {
                                            pushInfoBean.pushText = "同意交换联系方式";
                                        } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_CHANGE_CONTACT)) {
                                            pushInfoBean.pushText = "拒绝交换联系方式";
                                        }
                                        pushInfoBean.msg_type = PushInfoBean.CHAT_OTHER_TEXT_CMD_MSG;
                                        displayPushInfo(pushInfoBean);
                                    }
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
                                    PushInfoBean pushInfoBean = new PushInfoBean();
                                    pushInfoBean.senderUserId = senderUserId;
                                    pushInfoBean.pushText = "向您发送了一张图片";
                                    pushInfoBean.msg_type = PushInfoBean.CHAT_IMG_MSG;
                                    displayPushInfo(pushInfoBean);
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
                                    PushInfoBean pushInfoBean = new PushInfoBean();
                                    pushInfoBean.senderUserId = senderUserId;
                                    pushInfoBean.pushText = "向您发送了一段语音";
                                    pushInfoBean.msg_type = PushInfoBean.CHAT_VOICE_MSG;
                                    displayPushInfo(pushInfoBean);
                                }
                            }
                        });
                    } else if (objectName.equals("RC:CmdMsg")) {
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                CommandMessage commandMessage = (CommandMessage) message.getContent();
                                String name = commandMessage.getName();
                                //相关任务消息,需要进行存储
                                if (name.equals("taskInfo")) {
                                    //先要往本地保存
                                    File dataDir = new File(CommonUtils.getContext().getFilesDir(), "relatedTaskDir");
                                    LogKit.v("getFilesDir():" + dataDir.getAbsolutePath());
                                    if (!dataDir.exists()) {
                                        dataDir.mkdirs();
                                    }
                                    File relatedTaskFiles = new File(dataDir, LoginManager.currentLoginUserId + "to" + targetId);

                                    FileOutputStream fos = null;
                                    OutputStreamWriter osw = null;
                                    BufferedWriter bw = null;
                                    try {
                                        relatedTaskFiles.createNewFile();

                                        String jsonData = commandMessage.getData();
                                        fos = new FileOutputStream(relatedTaskFiles);
                                        osw = new OutputStreamWriter(fos);
                                        bw = new BufferedWriter(osw);
                                        bw.append(jsonData);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        IOUtils.close(bw);
                                        IOUtils.close(osw);
                                        IOUtils.close(fos);
                                    }
                                    if (mRelatedTaskListener != null) {
                                        mRelatedTaskListener.displayRelatedTask();
                                    }
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
        if (mHistoryListener != null) {
            RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, 20, new RongIMClient.ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
//                    LogKit.v("Message Count:" + messages.size());
//                    for (Message message : messages) {
//                        LogKit.v("SenderId:" + message.getSenderUserId() + "  sentTime:" + message.getSentTime() + "   Direction:" + message.getMessageDirection() + " objectName:" + message.getObjectName());
//                    }
                    mHistoryListener.displayHistory(messages);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogKit.v("getHisMsgFromLocal error");
                }
            });
        }
    }

    /**
     * 从融云服务器远程获取聊天历史记录
     */
    private static void getHisMsgFromRemote() {
        if (mHistoryListener != null) {
            /**
             * 根据会话类型的目标 Id，回调方式获取某消息类型标识的N条历史消息记录。
             *
             * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM 聊天室会话类型。
             * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 。
             * @param dateTime         从该时间点开始获取消息。即：消息中的 sentTime；第一次可传 0，获取最新 count 条。
             * @param count            要获取的消息数量，最多 20 条。
             * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
             */
            RongIMClient.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, System.currentTimeMillis(), 5, new RongIMClient.ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
//                    System.currentTimeMillis()
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
    }


    //自定义的各种聊天消息的监听器

    public static String targetId = "-1";
    public static String targetName;
    public static String targetAvatar;//聊天目标的头像url（fileId）
    public static int targetAvatarResource = -1;//如果聊天目标是斜杠小助手，头像用本地的资源文件

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

    public interface HistoryListener {
        public void displayHistory(List<Message> messages);
    }

    public interface RelatedTaskListener {
        public void displayRelatedTask();
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

    public static void setHistoryListener(HistoryListener historyListener) {
        mHistoryListener = historyListener;
    }

    public static void removeHistoryListener() {
        mHistoryListener = null;
    }

    public static void setRelatedTaskListener(RelatedTaskListener relatedTaskListener) {
        mRelatedTaskListener = relatedTaskListener;
    }

    public static void removeRelatedTaskListener() {
        mRelatedTaskListener = null;
    }


    /**
     * 三、[消息系统]-获得会话列表
     *
     * @param onGetConversationListFinished
     * @param offset                        请求偏移量
     * @param limit                         分页请求数量
     */
    public static void getConversationList(BaseProtocol.IResultExecutor onGetConversationListFinished, String offset, String limit) {
        ConversationListProtocol conversationListProtocol = new ConversationListProtocol(offset, limit);
        conversationListProtocol.getDataFromServer(onGetConversationListFinished);
    }

    /**
     * 获取融云token，暂时不去调用更新融云token接口（如果融云token失效，应该去调用更新融云token接口，但是调用后融云token并没有更新）
     * 需要传入的手机号参数，随便传一个就能成功，所以暂时先传一个假数据
     */
    public static void getRongToken(BaseProtocol.IResultExecutor onGetRongTokenFinished, String uid, String phone) {
        RongTokenProtocol rongTokenProtocol = new RongTokenProtocol(uid, phone);
        rongTokenProtocol.getDataFromServer(onGetRongTokenFinished);
    }

    /**
     * 更新融云token
     *
     * @param onRefreshRongTokenFinished
     * @param uid
     * @param phone
     */
    public static void refreshRongToken(BaseProtocol.IResultExecutor onRefreshRongTokenFinished, String uid, String phone) {
        RefreshRongTokenProtocol refreshRongTokenProtocol = new RefreshRongTokenProtocol(uid, phone);
        refreshRongTokenProtocol.getDataFromServer(onRefreshRongTokenFinished);
    }

    /**
     * 是否交换过手机号
     */
    public static void getIsChangeContact(BaseProtocol.IResultExecutor onGetIsChangeContactFinished, String uid) {
        GetIsChangeContactProtocol getIsChangeContactProtocol = new GetIsChangeContactProtocol(uid);
        getIsChangeContactProtocol.getDataFromServer(onGetIsChangeContactFinished);
    }

    /**
     * 设置已经交换了手机号标识
     */
    public static void setChangeContact(BaseProtocol.IResultExecutor onSetChangeContactFinished, String uid) {
        SetChangeContactProtocol setChangeContactProtocol = new SetChangeContactProtocol(uid);
        setChangeContactProtocol.getDataFromServer(onSetChangeContactFinished);
    }

    /**
     * 二、[好友]-发起好友申请
     *
     * @param uid
     * @param extra
     */
    public static void addFriend(BaseProtocol.IResultExecutor onAddFriendFinished, String uid, String extra) {
        AddFriendProtocol addFriendProtocol = new AddFriendProtocol(uid, extra);
        addFriendProtocol.getDataFromServer(onAddFriendFinished);
    }


    /**
     * 三、[好友]-同意好友申请
     */
    public static void agreeAddFriend(BaseProtocol.IResultExecutor onAgreeAddFriendFinished, String uid, String extra) {
        AgreeAddFriendProtocol agreeAddFriendProtocol = new AgreeAddFriendProtocol(uid, extra);
        agreeAddFriendProtocol.getDataFromServer(onAgreeAddFriendFinished);
    }

    /**
     * 四、[好友]-拒绝好友申请
     */
    public static void rejectAddFriend(BaseProtocol.IResultExecutor onRejectAddFriendFinished, String uid, String extra) {
        RejectAddFriendProtocol rejectAddFriendProtocol = new RejectAddFriendProtocol(uid, extra);
        rejectAddFriendProtocol.getDataFromServer(onRejectAddFriendFinished);
    }

    /**
     * 十、[好友]-好友申请状态查询  （获取添加好友的申请状态）
     */
    public static void getAddFriendStatus(BaseProtocol.IResultExecutor onGetAddFriendStatusFinished, String uid) {
        AddFriendStatusProtocol addFriendStatusProtocol = new AddFriendStatusProtocol(uid);
        addFriendStatusProtocol.getDataFromServer(onGetAddFriendStatusFinished);
    }

}
