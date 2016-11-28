package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatInfoBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatInfoModel extends BaseObservable {

    ItemChatInfoBinding mItemChatInfoBinding;
    Activity mActivity;

    public ChatInfoModel(ItemChatInfoBinding itemChatInfoBinding, Activity activity) {
        this.mItemChatInfoBinding = itemChatInfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    private String info;

    @Bindable
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
        notifyPropertyChanged(BR.info);
    }
}
