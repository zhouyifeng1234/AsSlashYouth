package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMyTextBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyTextModel extends BaseObservable {

    ItemChatMyTextBinding mItemChatMyTextBinding;
    Activity mActivity;
    boolean mIsRead;
    TextMessage mTextMessage;
    String mTargetId;
    boolean isFail;

    public ChatMyTextModel(ItemChatMyTextBinding itemChatMyTextBinding, Activity activity, String inputText, boolean isRead, TextMessage textMessage, String targetId, boolean isFail) {
        this.mItemChatMyTextBinding = itemChatMyTextBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;
        this.mTextMessage = textMessage;
        this.mTargetId = targetId;
        this.isFail = isFail;

        setMySendText(inputText);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMyTextBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMyTextBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMyTextBinding.tvChatMsgReadStatus.setText("已读");
            mItemChatMyTextBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_readed_marker_bg);
        } else {
//            mItemChatMyTextBinding.tvChatMsgReadStatus.setText("未读");
            if (isFail) {
                mItemChatMyTextBinding.ivChatSendMsgAgain.setVisibility(View.VISIBLE);
            } else {
                mItemChatMyTextBinding.tvChatMsgReadStatus.setText("送达");
                mItemChatMyTextBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_unreaded_marker_bg);
            }
        }
    }

    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mTextMessage != null && !TextUtils.isEmpty(mTargetId)) {
            String inputText = mTextMessage.getContent();
            String pushContent = LoginManager.currentLoginUserName + ":" + inputText;
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId, mTextMessage, pushContent, null, new RongIMClient.SendMessageCallback() {
                //发送消息的回调
                @Override
                public void onSuccess(Integer integer) {
                    mItemChatMyTextBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
                    //重新发送以后，需要在容易的消息记录里把原来这表messageId的消息删除掉
                    int messageId = (int) mItemChatMyTextBinding.ivChatSendMsgAgain.getTag();
                    int[] ids = new int[]{messageId};
                    RongIMClient.getInstance().deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //删除消息成功
                            //UI上面，要把这个View,提取到最下面
                            View messageView = mItemChatMyTextBinding.getRoot();
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

    private String mySendText;

    @Bindable
    public String getMySendText() {
        return mySendText;
    }

    public void setMySendText(String mySendText) {
        this.mySendText = mySendText;
        notifyPropertyChanged(BR.mySendText);
    }

}
