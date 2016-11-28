package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemPushInfoBinding;
import com.slash.youth.domain.PushInfoBean;

/**
 * Created by zhouyifeng on 2016/11/22.
 */
public class ItemPushInfoModel extends BaseObservable {
    ItemPushInfoBinding mItemPushInfoBinding;
    Activity mActivity;
    PushInfoBean mPushInfoBean;

    public ItemPushInfoModel(ItemPushInfoBinding itemPushInfoBinding, Activity activity, PushInfoBean pushInfoBean) {
        this.mItemPushInfoBinding = itemPushInfoBinding;
        this.mActivity = activity;
        mPushInfoBean = pushInfoBean;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        setPushText(mPushInfoBean.pushText);
    }

    private String pushText;

    @Bindable
    public String getPushText() {
        return pushText;
    }

    public void setPushText(String pushText) {
        this.pushText = pushText;
        notifyPropertyChanged(BR.pushText);
    }
}
