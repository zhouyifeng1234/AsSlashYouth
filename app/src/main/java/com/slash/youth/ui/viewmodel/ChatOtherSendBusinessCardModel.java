package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatOtherSendBusinessCardBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatOtherSendBusinessCardModel extends BaseObservable {

    ItemChatOtherSendBusinessCardBinding mItemChatOtherSendBusinessCardBinding;
    Activity mActivity;
    String mTargetAvatar;

    public ChatOtherSendBusinessCardModel(ItemChatOtherSendBusinessCardBinding itemChatOtherSendBusinessCardBinding, Activity activity, String targetAvatar) {
        this.mItemChatOtherSendBusinessCardBinding = itemChatOtherSendBusinessCardBinding;
        this.mActivity = activity;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if (!"1000".equals(MsgManager.targetId)) {
            BitmapKit.bindImage(mItemChatOtherSendBusinessCardBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
        } else {
            mItemChatOtherSendBusinessCardBinding.ivChatOtherAvatar.setImageResource(MsgManager.targetAvatarResource);
        }
    }

}
