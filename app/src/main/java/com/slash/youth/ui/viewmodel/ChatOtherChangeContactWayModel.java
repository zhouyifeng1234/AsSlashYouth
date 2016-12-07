package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemChatOtherChangeContactWayBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherChangeContactWayModel extends BaseObservable {

    ItemChatOtherChangeContactWayBinding mItemChatOtherChangeContactWayBinding;
    Activity mActivity;
    ChatModel mChatModel;
    String mOtherPhone;
    String mTargetAvatar;

    public ChatOtherChangeContactWayModel(ItemChatOtherChangeContactWayBinding itemChatOtherChangeContactWayBinding, Activity activity, ChatModel chatModel, String otherPhone, String targetAvatar) {
        this.mItemChatOtherChangeContactWayBinding = itemChatOtherChangeContactWayBinding;
        this.mActivity = activity;
        this.mChatModel = chatModel;
        this.mOtherPhone = otherPhone;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatOtherChangeContactWayBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
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
