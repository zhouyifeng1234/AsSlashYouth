package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherChangeContactWayBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherChangeContactWayModel extends BaseObservable {

    ItemChatOtherChangeContactWayBinding mItemChatOtherChangeContactWayBinding;
    Activity mActivity;

    public ChatOtherChangeContactWayModel(ItemChatOtherChangeContactWayBinding itemChatOtherChangeContactWayBinding, Activity activity) {
        this.mItemChatOtherChangeContactWayBinding = itemChatOtherChangeContactWayBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
