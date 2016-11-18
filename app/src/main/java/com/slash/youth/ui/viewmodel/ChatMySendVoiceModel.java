package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatMySendVoiceBinding;

/**
 * Created by zhouyifeng on 2016/11/18.
 */
public class ChatMySendVoiceModel extends BaseObservable {
    ItemChatMySendVoiceBinding mItemChatMySendVoiceBinding;
    Activity mActivity;

    public ChatMySendVoiceModel(ItemChatMySendVoiceBinding itemChatMySendVoiceBinding, Activity activity) {
        this.mItemChatMySendVoiceBinding = itemChatMySendVoiceBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

}
