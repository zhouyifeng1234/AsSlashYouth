package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatFriendTextBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatFriendTextModel extends BaseObservable {
    ItemChatFriendTextBinding mItemChatFriendTextBinding;
    Activity mActivity;
    String mTargetAvatar;

    public ChatFriendTextModel(ItemChatFriendTextBinding itemChatFriendTextBinding, Activity activity, String targetAvatar) {
        this.mItemChatFriendTextBinding = itemChatFriendTextBinding;
        this.mActivity = activity;
        this.mTargetAvatar = targetAvatar;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        BitmapKit.bindImage(mItemChatFriendTextBinding.ivChatOtherAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mTargetAvatar);
    }

    private String textContent;

    @Bindable
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        notifyPropertyChanged(BR.textContent);
    }
}
