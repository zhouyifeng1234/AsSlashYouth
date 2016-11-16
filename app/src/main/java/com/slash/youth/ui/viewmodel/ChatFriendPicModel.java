package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatFriendPicBinding;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatFriendPicModel extends BaseObservable {

    ItemChatFriendPicBinding mItemChatFriendPicBinding;
    Activity mActivity;

    public ChatFriendPicModel(ItemChatFriendPicBinding itemChatFriendPicBinding, Activity activity) {
        this.mItemChatFriendPicBinding = itemChatFriendPicBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

}
