package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatMyShareTaskBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMyShareTaskModel extends BaseObservable {

    ItemChatMyShareTaskBinding mItemChatMyShareTaskBinding;
    Activity mActivity;

    public ChatMyShareTaskModel(ItemChatMyShareTaskBinding itemChatMyShareTaskBinding, Activity activity) {
        this.mItemChatMyShareTaskBinding = itemChatMyShareTaskBinding;
        this.mActivity = activity;
        initData();
        initView();

    }

    private void initData() {

    }

    private void initView() {

    }
}
