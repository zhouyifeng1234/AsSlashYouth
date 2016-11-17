package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherSendAddFriendBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendAddFriendModel extends BaseObservable {

    ItemChatOtherSendAddFriendBinding mItemChatOtherSendAddFriendBinding;
    Activity mActivity;

    public ChatOtherSendAddFriendModel(ItemChatOtherSendAddFriendBinding itemChatOtherSendAddFriendBinding, Activity activity) {
        this.mItemChatOtherSendAddFriendBinding = itemChatOtherSendAddFriendBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
