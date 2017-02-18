package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMyShareTaskBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMyShareTaskModel extends BaseObservable {

    ItemChatMyShareTaskBinding mItemChatMyShareTaskBinding;
    Activity mActivity;
    boolean mIsRead;
    ChatCmdShareTaskBean chatCmdShareTaskBean;
    TextMessage mTextMessage;
    String mTargetId;
    boolean isFail;

    public ChatMyShareTaskModel(ItemChatMyShareTaskBinding itemChatMyShareTaskBinding, Activity activity, boolean isRead, ChatCmdShareTaskBean chatCmdShareTaskBean, TextMessage textMessage, String targetId, boolean isFail) {
        this.mItemChatMyShareTaskBinding = itemChatMyShareTaskBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;
        this.chatCmdShareTaskBean = chatCmdShareTaskBean;
        this.mTextMessage = textMessage;
        this.mTargetId = targetId;
        this.isFail = isFail;

        initData();
        initView();

    }

    private void initData() {

    }

    private void initView() {
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMyShareTaskBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMyShareTaskBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setText("已读");
            mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_readed_marker_bg);
        } else {
//            mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setText("未读");
            if (isFail) {
                mItemChatMyShareTaskBinding.ivChatSendMsgAgain.setVisibility(View.VISIBLE);
            } else {
                mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setText("送达");
                mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_unreaded_marker_bg);
            }
        }

        BitmapKit.bindImage(mItemChatMyShareTaskBinding.ivShareTaskAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + chatCmdShareTaskBean.avatar);
        mItemChatMyShareTaskBinding.tvShareTaskTitle.setText(chatCmdShareTaskBean.title);
        mItemChatMyShareTaskBinding.tvShareTaskQuote.setText(chatCmdShareTaskBean.quote);

    }


    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mTextMessage != null && !TextUtils.isEmpty(mTargetId)) {
            String pushContent = LoginManager.currentLoginUserName + ":[任务分享],请查看";
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId, mTextMessage, pushContent, null, new RongIMClient.SendMessageCallback() {
                //发送消息的回调
                @Override
                public void onSuccess(Integer integer) {
                    mItemChatMyShareTaskBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
                    //重新发送以后，需要在容易的消息记录里把原来这表messageId的消息删除掉
                    int messageId = (int) mItemChatMyShareTaskBinding.ivChatSendMsgAgain.getTag();
                    int[] ids = new int[]{messageId};
                    RongIMClient.getInstance().deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //删除消息成功
                            //UI上面，要把这个View,提取到最下面
                            View messageView = mItemChatMyShareTaskBinding.getRoot();
                            ViewGroup parent = (ViewGroup) messageView.getParent();
                            parent.removeView(messageView);
                            parent.addView(messageView);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }

                @Override
                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
