package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatMyShareTaskBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMyShareTaskModel extends BaseObservable {

    ItemChatMyShareTaskBinding mItemChatMyShareTaskBinding;
    Activity mActivity;
    boolean mIsRead;

    public ChatMyShareTaskModel(ItemChatMyShareTaskBinding itemChatMyShareTaskBinding, Activity activity, boolean isRead) {
        this.mItemChatMyShareTaskBinding = itemChatMyShareTaskBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;

        initData();
        initView();

    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatMyShareTaskBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);

        if (mIsRead) {
            mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setText("已读");
        } else {
            mItemChatMyShareTaskBinding.tvChatMsgReadStatus.setText("未读");
        }
    }
}
