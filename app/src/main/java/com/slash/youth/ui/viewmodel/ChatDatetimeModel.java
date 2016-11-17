package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatDatetimeBinding;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatDatetimeModel extends BaseObservable {

    ItemChatDatetimeBinding mItemChatDatetimeBinding;
    Activity mActivity;


    public ChatDatetimeModel(ItemChatDatetimeBinding itemChatDatetimeBinding, Activity activity) {
        this.mItemChatDatetimeBinding = itemChatDatetimeBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
