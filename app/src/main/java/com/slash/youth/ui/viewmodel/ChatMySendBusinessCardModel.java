package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemChatMySendBusinessCardBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatMySendBusinessCardModel extends BaseObservable {
    ItemChatMySendBusinessCardBinding mItemChatMySendBusinessCardBinding;
    Activity mActivity;

    public ChatMySendBusinessCardModel(ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding, Activity activity) {
        this.mItemChatMySendBusinessCardBinding = itemChatMySendBusinessCardBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
    }

}
