package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherSendVoiceBinding;

/**
 * Created by zhouyifeng on 2016/11/18.
 */
public class ChatOtherSendVoiceModel extends BaseObservable {
    ItemChatOtherSendVoiceBinding mItemChatOtherSendVoiceBinding;
    Activity mActivity;

    public ChatOtherSendVoiceModel(ItemChatOtherSendVoiceBinding itemChatOtherSendVoiceBinding, Activity activity) {
        this.mItemChatOtherSendVoiceBinding = itemChatOtherSendVoiceBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
