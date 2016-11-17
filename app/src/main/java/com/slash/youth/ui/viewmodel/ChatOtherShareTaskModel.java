package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherShareTaskBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherShareTaskModel extends BaseObservable {
    ItemChatOtherShareTaskBinding mItemChatOtherShareTaskBinding;
    Activity mActivity;

    public ChatOtherShareTaskModel(ItemChatOtherShareTaskBinding itemChatOtherShareTaskBinding, Activity activity) {
        this.mItemChatOtherShareTaskBinding = itemChatOtherShareTaskBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

}
