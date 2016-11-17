package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherSendBusinessCardBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendBusinessCardModel extends BaseObservable {

    ItemChatOtherSendBusinessCardBinding mItemChatOtherSendBusinessCardBinding;
    Activity mActivity;

    public ChatOtherSendBusinessCardModel(ItemChatOtherSendBusinessCardBinding itemChatOtherSendBusinessCardBinding, Activity activity) {
        this.mItemChatOtherSendBusinessCardBinding = itemChatOtherSendBusinessCardBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

}
