package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

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
}
