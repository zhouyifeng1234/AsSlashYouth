package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatChangeContactWayInfoBinding;

/**
 * Created by zhouyifeng on 2016/11/17.
 */
public class ChatChangeContactWayInfoModel extends BaseObservable {

    ItemChatChangeContactWayInfoBinding mItemChatChangeContactWayInfoBinding;
    Activity mActivity;

    public ChatChangeContactWayInfoModel(ItemChatChangeContactWayInfoBinding itemChatChangeContactWayInfoBinding, Activity activity, String name, String otherPhone) {
        this.mItemChatChangeContactWayInfoBinding = itemChatChangeContactWayInfoBinding;
        this.mActivity = activity;
        setOtherContactInfo(name + "的手机号：" + otherPhone);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    private String otherContactInfo;

    @Bindable
    public String getOtherContactInfo() {
        return otherContactInfo;
    }

    public void setOtherContactInfo(String otherContactInfo) {
        this.otherContactInfo = otherContactInfo;
        notifyPropertyChanged(BR.otherContactInfo);
    }
}
