package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemChatOtherSendAddFriendBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendAddFriendModel extends BaseObservable {

    ItemChatOtherSendAddFriendBinding mItemChatOtherSendAddFriendBinding;
    Activity mActivity;
    ChatModel mChatModel;

    public ChatOtherSendAddFriendModel(ItemChatOtherSendAddFriendBinding itemChatOtherSendAddFriendBinding, Activity activity, ChatModel chatModel) {
        this.mItemChatOtherSendAddFriendBinding = itemChatOtherSendAddFriendBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

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
