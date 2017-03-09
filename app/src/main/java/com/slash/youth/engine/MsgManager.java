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

import com.google.gson.Gson;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemPushInfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.PushInfoBean;
import com.slash.youth.domain.RongTokenBean;
import com.slash.youth.domain.SlashMessageExtraBean;
import com.slash.youth.domain.TaskMessageBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.AddFriendProtocol;
import com.slash.youth.http.protocol.AddFriendStatusProtocol;
import com.slash.youth.http.protocol.AgreeAddFriendProtocol;
import com.slash.youth.http.protocol.AskCustomerServiceProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.ConversationListProtocol;
import com.slash.youth.http.protocol.DelConversationListProtocol;
import com.slash.youth.http.protocol.GetIsChangeContactProtocol;
import com.slash.youth.http.protocol.RefreshRongTokenProtocol;
import com.slash.youth.http.protocol.RejectAddFriendProtocol;
import com.slash.youth.http.protocol.RongTokenProtocol;
import com.slash.youth.http.protocol.SetChangeContactProtocol;
import com.slash.youth.http.protocol.SetConversationListProtocol;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.ui.activity.MessageActivity;
import com.slash.youth.ui.activity.MyBidDemandActivity;
import com.slash.youth.ui.activity.MyBidServiceActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.activity.MyPublishServiceActivity;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.event.OffLineEvent;
import com.slash.youth.ui.pager.HomeInfoPager;
import com.slash.youth.ui.viewmodel.ItemPushInfoModel;
import com.slash.youth.ui.viewmodel.MessageModel;
import com.slash.youth.ui.viewmodel.PagerHomeInfoModel;
import com.slash.youth.utils.ActivityUtils;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.CommandNotificationMessage;
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
    private static SlashMessageListener mSlashMessageListener;
    private static TotalUnReadCountListener mTotalUnReadCountListener;

    private static int getRongTokenTimes = 0;
    public static int taskMessageCount = -1;
    public static HashMap<Long, Integer> everyTaskMessageCount = null;

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
                int value = connectionStatus.getValue();
                LogKit.v("----ConnectionStatusListener connMessage:" + connMessage);
                LogKit.v("----ConnectionStatusListener value:" + value);
                if (value == 3) {//下线通知
                    CommonUtils.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (ActivityUtils.currentActivity instanceof BaseActivity)
                                ((BaseActivity) ActivityUtils.currentActivity).offline();
                        }
                    });
                }
            }
        });

        //首先断开链接，然后才去链接融云
//        RongIMClient.getInstance().disconnect();//断开与融云服务器的连接。当调用此接口断开连接后，仍然可以接收 Push 消息。
        RongIMClient.getInstance().logout();//断开与融云服务器的连接，并且不再接收 Push 消息。

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
                        LoginManager.rongToken = rongToken;
                        SpUtils.setString("rongToken", rongToken);
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

    public static void exit() {
        RongIMClient.getInstance().disconnect();
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

            if (objectName.equals("RC:TxtMsg")) {
                TextMessage textMessage = (TextMessage) message.getContent();
                String content = textMessage.getContent();
                String extra = textMessage.getExtra();
                LogKit.v("TextMessage conent:" + content);
                LogKit.v("TextMessage extra:" + extra);
            } else if (objectName.equals("RC:CmdMsg")) {
                CommandMessage commandMessage = (CommandMessage) message.getContent();
                String name = commandMessage.getName();
                String data = commandMessage.getData();
                LogKit.v("CmdMsg name:" + name + " data:" + data);
            } else if (objectName.equals("RC:CmdNtf")) {
                CommandNotificationMessage commandNotificationMessage = (CommandNotificationMessage) message.getContent();
                String name = commandNotificationMessage.getName();
                String data = commandNotificationMessage.getData();
                LogKit.v("CmdNtf name:" + name + "  data:" + data);
            }


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

            if (senderUserId.equals("100")) {//系统推送账号
                if (objectName.equals("RC:CmdNtf")) {
                    CommandNotificationMessage commandNotificationMessage = (CommandNotificationMessage) message.getContent();
                    String cmdNtfData = commandNotificationMessage.getData();
                    if (cmdNtfData.contains("\"tid\"")) {
                        //总的任务消息数据的处理
                        if (taskMessageCount != -1) {
                            taskMessageCount++;
                            SpUtils.setInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, taskMessageCount);
                        } else {
                            taskMessageCount = SpUtils.getInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, 0);
                            taskMessageCount++;
                            SpUtils.setInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, taskMessageCount);
                        }
                        if (ActivityUtils.currentActivity instanceof HomeActivity && HomeActivity.currentCheckedPageNo == HomeActivity.PAGE_INFO) {
                            HomeInfoPager homeInfoPager = (HomeInfoPager) HomeActivity.currentCheckedPager;
                            PagerHomeInfoModel pagerHomeInfoModel = homeInfoPager.mPagerHomeInfoModel;
                            if (MsgManager.taskMessageCount > 0) {
                                pagerHomeInfoModel.setTaskMessageCountPointVisibility(View.VISIBLE);
                                pagerHomeInfoModel.setTaskMessageCount(MsgManager.taskMessageCount + "");
                            } else {
                                pagerHomeInfoModel.setTaskMessageCountPointVisibility(View.GONE);
                            }
                        }
                        //每一个任务对应的消息数量的处理
                        if (everyTaskMessageCount == null) {
                            everyTaskMessageCount = deserializeEveryTaskMessageCount();
                            if (everyTaskMessageCount == null) {
                                everyTaskMessageCount = new HashMap<Long, Integer>();
                            }
                        }
//                        CommandNotificationMessage commandNotificationMessage = (CommandNotificationMessage) message.getContent();
                        String data = commandNotificationMessage.getData();
                        Gson gson = new Gson();
                        TaskMessageBean taskMessageBean = gson.fromJson(data, TaskMessageBean.class);
                        long id = taskMessageBean.id;
                        Integer integer = everyTaskMessageCount.get(id);
                        int count;
                        if (integer == null) {
                            count = 0;
                        } else {
                            count = integer;
                        }
                        count++;
                        everyTaskMessageCount.put(id, count);
                        //次数更新了，重新序列化到磁盘
                        serializeEveryTaskMessageCount(everyTaskMessageCount);

                        //如果当前页面是任务订单详情页，则实时刷新页面数据
                        if (taskMessageBean.type == 1) {//需求订单页
                            if (ActivityUtils.currentActivity instanceof MyBidDemandActivity) {
                                MyBidDemandActivity currentActivity = (MyBidDemandActivity) ActivityUtils.currentActivity;
                                currentActivity.mMyBidDemandModel.reloadData(true);
                            } else if (ActivityUtils.currentActivity instanceof MyPublishDemandActivity) {
                                MyPublishDemandActivity currentActivity = (MyPublishDemandActivity) ActivityUtils.currentActivity;
                                currentActivity.mMyPublishDemandModel.reloadData(true);
                            }
                        } else if (taskMessageBean.type == 2) {//服务订单页
                            if (ActivityUtils.currentActivity instanceof MyBidServiceActivity) {
                                MyBidServiceActivity currentActivity = (MyBidServiceActivity) ActivityUtils.currentActivity;
                                currentActivity.mMyBidServiceModel.reloadData(true);
                            } else if (ActivityUtils.currentActivity instanceof MyPublishServiceActivity) {
                                MyPublishServiceActivity currentActivity = (MyPublishServiceActivity) ActivityUtils.currentActivity;
                                currentActivity.mMyPublishServiceModel.reloadData(true);
                            }
                        }

                        //100号的消息数量，必须等到上面的代码执行完毕，才会更新，所以在这里去刷新消息数
                        //获取总的未读消息数
                        if (mTotalUnReadCountListener != null) {
                            RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                                @Override
                                public void onSuccess(Integer integer) {
                                    int totalUnreadCount = integer;
                                    mTotalUnReadCountListener.displayTotalUnReadCount(totalUnreadCount);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            }, Conversation.ConversationType.PRIVATE);
                        }
                    }
                }
            } else if (senderUserId.equals("1000")) {//斜杠消息助手
                CommonUtils.getHandler().post(new Runnable() {
                    public void run() {

                        //目前还不知道后台斜杠小助手发送过来的内容是什么格式，暂时认为它是"RC:TxtMsg"，其中的content就是内容
                        if (objectName.equals("RC:TxtMsg")) {

                            updateConversationList(senderUserId);

                            //获取总的未读消息数
                            if (mTotalUnReadCountListener != null) {
                                RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                                    @Override
                                    public void onSuccess(Integer integer) {
                                        int totalUnreadCount = integer;
                                        mTotalUnReadCountListener.displayTotalUnReadCount(totalUnreadCount);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                }, Conversation.ConversationType.PRIVATE);
                            }


                            TextMessage textMessage = (TextMessage) message.getContent();
                            String extraInfo = textMessage.getExtra();
                            if (mSlashMessageListener != null && targetId.equals(senderUserId)) {
                                mSlashMessageListener.displayMessage(message, left);
                            } else {
                                //消息推送的顶部弹框提示
                                PushInfoBean pushInfoBean = new PushInfoBean();
                                pushInfoBean.pushText = textMessage.getContent();
                                pushInfoBean.msg_type = PushInfoBean.CHAT_TEXT_MSG;
                                try {
                                    Gson gson = new Gson();
                                    SlashMessageExtraBean slashMessageExtraBean = gson.fromJson(extraInfo, SlashMessageExtraBean.class);
                                    pushInfoBean.senderUserId = slashMessageExtraBean.uid + "";
                                } catch (Exception ex) {
                                    pushInfoBean.senderUserId = "1000";
                                }
                                displayPushInfo(pushInfoBean);
                            }
                        }
                    }
                });
            } else if (senderUserId.equals(customerServiceUid)) {//斜杠客服助手,目前任务消息类型是TextMessage，内容都在content里面
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateConversationList(senderUserId);
                        if (objectName.equals("RC:TxtMsg")) {
                            TextMessage textMessage = (TextMessage) message.getContent();
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
                            LogKit.v("斜杠客服助手发来的不是TextMessage格式的内容");
                        }
                    }
                });
            } else {//聊天消息
                if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {//判断是单聊消息

                    //获取总的未读消息数
                    if (mTotalUnReadCountListener != null) {
                        RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                            @Override
                            public void onSuccess(Integer integer) {
                                int totalUnreadCount = integer;
                                mTotalUnReadCountListener.displayTotalUnReadCount(totalUnreadCount);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        }, Conversation.ConversationType.PRIVATE);
                    }

                    CommonUtils.getHandler().post(new Runnable() {
                        public void run() {
                            updateConversationList(senderUserId);
                        }
                    });
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
                                        if (relatedTaskFiles.exists()) {
                                            relatedTaskFiles.delete();
                                        }
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

    public static ArrayList<String> conversationUidList = new ArrayList<String>();

    public static void updateConversationList(String senderUserId) {
        int index = conversationUidList.indexOf(senderUserId);
        if (index != 0) {
            if (index > 0) {//会话列表中本来就存在，但是不在第一个，要提取到第一个
                conversationUidList.remove(senderUserId);
            }
            conversationUidList.add(0, senderUserId);
            //会话列表顺序发生了变化，需要调用更新接口
            ArrayList<Long> updateConversationUidList = new ArrayList<Long>();
            updateConversationUidList.add(Long.parseLong(senderUserId));
            setConversationList(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {

                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("跟新会话列表失败");
                }
            }, updateConversationUidList);
        }
        //页面展示上的更新
        //这里的代码是对应V1.0版的旧的首页中的消息页
//        if (ActivityUtils.currentActivity instanceof HomeActivity && HomeActivity.currentCheckedPageNo == HomeActivity.PAGE_INFO) {
//            HomeInfoPager homeInfoPager = (HomeInfoPager) HomeActivity.currentCheckedPager;
//            PagerHomeInfoModel pagerHomeInfoModel = homeInfoPager.mPagerHomeInfoModel;
//            if (index != 0) {//列表顺序发生了变化，需要重新调用接口获取列表
//                pagerHomeInfoModel.getDataFromServer();
//            } else {//重新设置列表数据，主要用来更新最近一条消息的展示
//                pagerHomeInfoModel.setConversationList();
//            }
//        }
        if (ActivityUtils.currentActivity instanceof MessageActivity) {
            MessageActivity messageActivity = (MessageActivity) ActivityUtils.currentActivity;
            MessageModel messageModel = messageActivity.getMessageModel();
            if (index != 0) {//列表顺序发生了变化，需要重新调用接口获取列表
                messageModel.getDataFromServer();
            } else {//重新设置列表数据，主要用来更新最近一条消息的展示
                messageModel.setConversationList();
            }
        }
    }

    /**
     * 弹出消息的逻辑
     *
     * @param pushInfoBean
     */
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

    public static void loadHistoryChatRecord(int oldestMessageId) {
        getHisMsgFromLocal(oldestMessageId);
//        getHisMsgFromRemote();
    }

    /**
     * 从客户端本地读取聊天记录
     */
    private static void getHisMsgFromLocal(int oldestMessageId) {
        if (mHistoryListener != null) {
//            RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, 20, new RongIMClient.ResultCallback<List<Message>>() {
//                @Override
//                public void onSuccess(List<Message> messages) {
////                    LogKit.v("Message Count:" + messages.size());
////                    for (Message message : messages) {
////                        LogKit.v("SenderId:" + message.getSenderUserId() + "  sentTime:" + message.getSentTime() + "   Direction:" + message.getMessageDirection() + " objectName:" + message.getObjectName());
////                    }
//                    mHistoryListener.displayHistory(messages);
//                }
//
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//                    LogKit.v("getHisMsgFromLocal error");
//                }
//            });

            RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, oldestMessageId, 20, new RongIMClient.ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
                    if (mHistoryListener != null) {
                        mHistoryListener.displayHistory(messages);
                    }
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
    public static String customerServiceUid;//保存随机获取客服ID

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

    public interface TotalUnReadCountListener {
        public void displayTotalUnReadCount(int count);
    }

    /**
     * 斜杠消息助手（1000号）消息监听器
     */
    public interface SlashMessageListener {
        public void displayMessage(Message message, int left);
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

    public static void setSlashMessageListener(SlashMessageListener slashMessageListener) {
        mSlashMessageListener = slashMessageListener;
    }

    public static void removeSlashMessageListener() {
        mSlashMessageListener = null;
    }

    public static void setTotalUnReadCountListener(TotalUnReadCountListener totalUnReadCountListener) {
        mTotalUnReadCountListener = totalUnReadCountListener;
    }

    public static void removeTotalUnReadCountListener() {
        mTotalUnReadCountListener = null;
    }

    /**
     * 序列化每个任务对应的消息数量的HashMap
     */
    public static void serializeEveryTaskMessageCount(HashMap<Long, Integer> hashEveryTaskMessageCount) {
        File fileCache = CommonUtils.getContext().getCacheDir();
        if (!fileCache.exists()) {
            fileCache.mkdirs();
        }
        File fileTaskMessage = new File(fileCache, "everyTaskMessageCount");
        FileOutputStream fosTaskMessage = null;
        ObjectOutputStream oosTaskMessage = null;
        try {
            fosTaskMessage = new FileOutputStream(fileTaskMessage);
            oosTaskMessage = new ObjectOutputStream(fosTaskMessage);
            oosTaskMessage.writeObject(hashEveryTaskMessageCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oosTaskMessage);
            IOUtils.close(fosTaskMessage);
        }
    }

    /**
     * 反序列化每个任务对应的消息数量的HashMap
     */
    public static HashMap<Long, Integer> deserializeEveryTaskMessageCount() {
        File fileCache = CommonUtils.getContext().getCacheDir();
        File fileTagJson = new File(fileCache, "everyTaskMessageCount");
        if (!fileTagJson.exists()) {
            return null;
        } else {
            FileInputStream fisTaskMessage = null;
            ObjectInputStream oisTaskMessage = null;
            try {
                fisTaskMessage = new FileInputStream(fileTagJson);
                oisTaskMessage = new ObjectInputStream(fisTaskMessage);
                HashMap<Long, Integer> hashEveryTaskMessageCount = (HashMap<Long, Integer>) oisTaskMessage.readObject();
                return hashEveryTaskMessageCount;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(oisTaskMessage);
                IOUtils.close(fisTaskMessage);
            }
        }
        return null;
    }

    /**
     * 五、[消息系统]-获得会话列表
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
     * 六、[消息系统]-更新会话列表
     *
     * @param onSetConversationListFinished
     * @param conversationUidList
     */
    public static void setConversationList(BaseProtocol.IResultExecutor onSetConversationListFinished, ArrayList<Long> conversationUidList) {
        SetConversationListProtocol setConversationListProtocol = new SetConversationListProtocol(conversationUidList);
        setConversationListProtocol.getDataFromServer(onSetConversationListFinished);
    }

    public static void delConversationList(BaseProtocol.IResultExecutor onDelConversationListFinished, ArrayList<Long> conversationUidList) {
        DelConversationListProtocol delConversationListProtocol = new DelConversationListProtocol(conversationUidList);
        delConversationListProtocol.getDataFromServer(onDelConversationListFinished);
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

    /**
     * 从SharePreferences中读取客服Uid
     *
     * @return
     */
    public static long getCustomerServiceUidFromSp() {
        long customerServiceUid = SpUtils.getLong(GlobalConstants.SpConfigKey.CUSTOMER_SERVICE_UID_KEY, -1);
        return customerServiceUid;
    }

    /**
     * 将获取到的客服Uid保存到SharePreferences中
     *
     * @param customerServiceUid
     */
    public static void setCustomerServiceUidToSp(long customerServiceUid) {
        SpUtils.setLong(GlobalConstants.SpConfigKey.CUSTOMER_SERVICE_UID_KEY, customerServiceUid);
    }

    /**
     * 请求客服，可以或得随机客服的UID
     */
    public static void getCustomerService(BaseProtocol.IResultExecutor onGetCustomerServiceFinished) {
        AskCustomerServiceProtocol askCustomerServiceProtocol = new AskCustomerServiceProtocol();
        askCustomerServiceProtocol.getDataFromServer(onGetCustomerServiceFinished);
    }

}
