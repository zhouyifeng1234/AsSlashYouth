package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatMyTextBinding;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatMyTextModel extends BaseObservable {

    ItemChatMyTextBinding mItemChatMyTextBinding;
    Activity mActivity;

    public ChatMyTextModel(ItemChatMyTextBinding itemChatMyTextBinding, Activity activity, String inputText) {
        this.mItemChatMyTextBinding = itemChatMyTextBinding;
        this.mActivity = activity;

        setMySendText(inputText);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

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
