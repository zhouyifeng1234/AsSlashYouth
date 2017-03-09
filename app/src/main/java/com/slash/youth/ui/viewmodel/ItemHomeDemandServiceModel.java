package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.opengl.Visibility;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class ItemHomeDemandServiceModel extends BaseObservable {

    ItemHomeDemandServiceBinding mItemHomeDemandServiceBinding;

    public ItemHomeDemandServiceModel(ItemHomeDemandServiceBinding itemHomeDemandServiceBinding) {
        this.mItemHomeDemandServiceBinding = itemHomeDemandServiceBinding;
        initView();
        initData();

    }

    private void initView() {

    }

    private void initData() {

    }

    private String demandOrServiceTime;
    private int demandReplyTimeVisibility;

    @Bindable
    public String getDemandOrServiceTime() {
        return demandOrServiceTime;
    }

    public void setDemandOrServiceTime(String demandOrServiceTime) {
        this.demandOrServiceTime = demandOrServiceTime;
        notifyPropertyChanged(BR.demandOrServiceTime);
    }

    @Bindable
    public int getDemandReplyTimeVisibility() {
        return demandReplyTimeVisibility;
    }

    public void setDemandReplyTimeVisibility(int demandReplyTimeVisibility) {
        this.demandReplyTimeVisibility = demandReplyTimeVisibility;
        notifyPropertyChanged(BR.demandReplyTimeVisibility);
    }
}
