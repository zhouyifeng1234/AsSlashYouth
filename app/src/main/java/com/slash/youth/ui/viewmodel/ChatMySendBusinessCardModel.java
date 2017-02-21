package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemChatMySendBusinessCardBinding;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMySendBusinessCardModel extends BaseObservable {
    ItemChatMySendBusinessCardBinding mItemChatMySendBusinessCardBinding;
    Activity mActivity;
    boolean mIsRead;
    ChatCmdBusinesssCardBean mChatCmdBusinesssCardBean;
    TextMessage mTextMessage;
    String mTargetId;
    boolean isFail;

    public ChatMySendBusinessCardModel(ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding, Activity activity, boolean isRead, ChatCmdBusinesssCardBean chatCmdBusinesssCardBean, TextMessage textMessage, String targetId, boolean isFail) {
        this.mItemChatMySendBusinessCardBinding = itemChatMySendBusinessCardBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;
        this.mChatCmdBusinesssCardBean = chatCmdBusinesssCardBean;
        this.mTextMessage = textMessage;
        this.mTargetId = targetId;
        this.isFail = isFail;

        initData();
        initView();
    }

    private void initData() {
        //显示名片内容
        BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivBusinessCardAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mChatCmdBusinesssCardBean.avatar);
        mItemChatMySendBusinessCardBinding.tvBusinessCardName.setText(mChatCmdBusinesssCardBean.name);
        String industryAndProfession = "";
        if (!TextUtils.isEmpty(mChatCmdBusinesssCardBean.industry) && !TextUtils.isEmpty(mChatCmdBusinesssCardBean.profession)) {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + "|" + mChatCmdBusinesssCardBean.profession;
        } else {
            industryAndProfession = mChatCmdBusinesssCardBean.industry + mChatCmdBusinesssCardBean.profession;
        }
        mItemChatMySendBusinessCardBinding.tvBusinessIndustryProfession.setText(industryAndProfession);
    }

    private void initView() {
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMySendBusinessCardBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("已读");
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_readed_marker_bg);
        } else {
//            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("未读");
            if (isFail) {
                mItemChatMySendBusinessCardBinding.ivChatSendMsgAgain.setVisibility(View.VISIBLE);
            } else {
                mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("送达");
                mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setBackgroundResource(R.drawable.shape_chat_text_unreaded_marker_bg);
            }
        }
    }

    public void gotoUserInfoPage(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", Long.parseLong(mChatCmdBusinesssCardBean.uid));
        mActivity.startActivity(intentUserInfoActivity);
    }

    //重新发送消息
    public void sendMsgAgain(View v) {
        if (mTextMessage != null && !TextUtils.isEmpty(mTargetId)) {
            String pushContent = LoginManager.currentLoginUserName + ":[名片],请查看";
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, mTargetId, mTextMessage, pushContent, null, new RongIMClient.SendMessageCallback() {
                //发送消息的回调
                @Override
                public void onSuccess(Integer integer) {
                    mItemChatMySendBusinessCardBinding.ivChatSendMsgAgain.setVisibility(View.GONE);
                    //重新发送以后，需要在容易的消息记录里把原来这表messageId的消息删除掉
                    int messageId = (int) mItemChatMySendBusinessCardBinding.ivChatSendMsgAgain.getTag();
                    int[] ids = new int[]{messageId};
                    RongIMClient.getInstance().deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //删除消息成功
                            //UI上面，要把这个View,提取到最下面
                            View messageView = mItemChatMySendBusinessCardBinding.getRoot();
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
