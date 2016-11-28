package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatMySendBusinessCardBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMySendBusinessCardModel extends BaseObservable {
    ItemChatMySendBusinessCardBinding mItemChatMySendBusinessCardBinding;
    Activity mActivity;

    public ChatMySendBusinessCardModel(ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding, Activity activity) {
        this.mItemChatMySendBusinessCardBinding = itemChatMySendBusinessCardBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

}
