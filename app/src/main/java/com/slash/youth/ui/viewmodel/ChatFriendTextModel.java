package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatFriendTextBinding;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatFriendTextModel extends BaseObservable {
    ItemChatFriendTextBinding mItemChatFriendTextBinding;
    Activity mActivity;

    public ChatFriendTextModel(ItemChatFriendTextBinding itemChatFriendTextBinding, Activity activity) {
        this.mItemChatFriendTextBinding = itemChatFriendTextBinding;
        this.mActivity = activity;


        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

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
