package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatMyTextBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyTextModel extends BaseObservable {

    ItemChatMyTextBinding mItemChatMyTextBinding;
    Activity mActivity;
    boolean mIsRead;

    public ChatMyTextModel(ItemChatMyTextBinding itemChatMyTextBinding, Activity activity, String inputText, boolean isRead) {
        this.mItemChatMyTextBinding = itemChatMyTextBinding;
        this.mActivity = activity;
        this.mIsRead = isRead;

        setMySendText(inputText);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatMyTextBinding.ivChatMyAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + LoginManager.currentLoginUserAvatar);

        if (mIsRead) {
            mItemChatMyTextBinding.tvChatMsgReadStatus.setText("已读");
        } else {
            mItemChatMyTextBinding.tvChatMsgReadStatus.setText("未读");
        }
    }

    private String mySendText;

    @Bindable
    public String getMySendText() {
        return mySendText;
    }

    public void setMySendText(String mySendText) {
        this.mySendText = mySendText;
        notifyPropertyChanged(BR.mySendText);
    }
}
