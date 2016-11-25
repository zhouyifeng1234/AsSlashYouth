package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemChatOtherChangeContactWayBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherChangeContactWayModel extends BaseObservable {

    ItemChatOtherChangeContactWayBinding mItemChatOtherChangeContactWayBinding;
    Activity mActivity;
    ChatModel mChatModel;
    String mOtherPhone;

    public ChatOtherChangeContactWayModel(ItemChatOtherChangeContactWayBinding itemChatOtherChangeContactWayBinding, Activity activity, ChatModel chatModel, String otherPhone) {
        this.mItemChatOtherChangeContactWayBinding = itemChatOtherChangeContactWayBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        this.mOtherPhone = otherPhone;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    //同意交换联系方式
    public void agreeChangeContact(View v) {
        mChatModel.agreeChangeContact(mOtherPhone);
    }

    //拒绝交换联系方式
    public void refuseChangeContact(View v) {
        mChatModel.refuseChangeContact();
    }
}
