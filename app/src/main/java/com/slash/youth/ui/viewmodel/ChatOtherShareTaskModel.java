package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherShareTaskBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherShareTaskModel extends BaseObservable {
    ItemChatOtherShareTaskBinding mItemChatOtherShareTaskBinding;
    Activity mActivity;
    String mTargetAvatar;
    ChatCmdShareTaskBean chatCmdShareTaskBean;

    public ChatOtherShareTaskModel(ItemChatOtherShareTaskBinding itemChatOtherShareTaskBinding, Activity activity, String targetAvatar, ChatCmdShareTaskBean chatCmdShareTaskBean) {
        this.mItemChatOtherShareTaskBinding = itemChatOtherShareTaskBinding;
        this.mActivity = activity;
        this.mTargetAvatar = targetAvatar;
        this.chatCmdShareTaskBean = chatCmdShareTaskBean;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if ((!"1000".equals(MsgManager.targetId)) && (!MsgManager.customerServiceUid.equals(MsgManager.targetId))) {
            BitmapKit.bindImage(mItemChatOtherShareTaskBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherShareTaskBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }

        BitmapKit.bindImage(mItemChatOtherShareTaskBinding.ivShareTaskAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + chatCmdShareTaskBean.avatar);
        mItemChatOtherShareTaskBinding.tvShareTitle.setText(chatCmdShareTaskBean.title);
        mItemChatOtherShareTaskBinding.tvShareTaskQuote.setText(chatCmdShareTaskBean.quote);

    }

}
