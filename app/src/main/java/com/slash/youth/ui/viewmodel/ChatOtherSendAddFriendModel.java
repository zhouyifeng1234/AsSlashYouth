package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemChatOtherSendAddFriendBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendAddFriendModel extends BaseObservable {

    ItemChatOtherSendAddFriendBinding mItemChatOtherSendAddFriendBinding;
    Activity mActivity;
    ChatModel mChatModel;
    String mTargetAvatar;

    public ChatOtherSendAddFriendModel(ItemChatOtherSendAddFriendBinding itemChatOtherSendAddFriendBinding, Activity activity, ChatModel chatModel, String targetAvatar) {
        this.mItemChatOtherSendAddFriendBinding = itemChatOtherSendAddFriendBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatOtherSendAddFriendBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
    }

    //同意添加对方为好友
    public void agreeAddFriend(View v) {
        mChatModel.agreeAddFriend();
    }

    //拒绝添加对方为好友
    public void refuseAddFriend(View v) {
        mChatModel.refuseAddFriend();
    }
}
