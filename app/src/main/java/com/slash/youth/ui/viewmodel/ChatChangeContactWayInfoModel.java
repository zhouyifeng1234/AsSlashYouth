package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatChangeContactWayInfoBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatChangeContactWayInfoModel extends BaseObservable {

    ItemChatChangeContactWayInfoBinding mItemChatChangeContactWayInfoBinding;
    Activity mActivity;

    public ChatChangeContactWayInfoModel(ItemChatChangeContactWayInfoBinding itemChatChangeContactWayInfoBinding, Activity activity) {
        this.mItemChatChangeContactWayInfoBinding = itemChatChangeContactWayInfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
