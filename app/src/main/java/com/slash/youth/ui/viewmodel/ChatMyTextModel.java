package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

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

    public ChatMyTextModel(ItemChatMyTextBinding itemChatMyTextBinding, Activity activity, String inputText, boolean isRead, TextMessage textMessage, String targetId) {
        this.mItemChatMyTextBinding = itemChatMyTextBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;
        this.mTextMessage = textMessage;
        this.mTargetId = targetId;

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
        } else {
            mItemChatMyTextBinding.tvChatMsgReadStatus.setText("未读");
        }
    }

    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mTextMessage != null && !TextUtils.isEmpty(mTargetId)) {
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId, mTextMessage, null, null, new RongIMClient.SendMessageCallback() {
                //发送消息的回调
                @Override
                public void onSuccess(Integer integer) {
                    mItemChatMyTextBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
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
