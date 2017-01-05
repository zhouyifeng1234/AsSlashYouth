package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.slash.youth.R;
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
    boolean mIsRead;


    public ChatMySendBusinessCardModel(ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding, Activity activity, boolean isRead) {
        this.mItemChatMySendBusinessCardBinding = itemChatMySendBusinessCardBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        if (!TextUtils.isEmpty(LoginManager.currentLoginUserAvatar)) {
            BitmapKit.bindImage(mItemChatMySendBusinessCardBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);
        } else {
            mItemChatMySendBusinessCardBinding.ivChatMyAvatar.setImageResource(R.mipmap.default_avatar);
        }

        if (mIsRead) {
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("已读");
        } else {
            mItemChatMySendBusinessCardBinding.tvChatMsgReadStatus.setText("未读");
        }
    }

}
