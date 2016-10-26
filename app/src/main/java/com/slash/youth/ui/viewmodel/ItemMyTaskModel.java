package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemMyTaskBinding;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class ItemMyTaskModel extends BaseObservable {
    ItemMyTaskBinding mItemMyTaskBinding;

    public ItemMyTaskModel(ItemMyTaskBinding itemMyTaskBinding) {
        this.mItemMyTaskBinding = itemMyTaskBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }
}
