package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemUserinfoBinding;

/**
 * Created by zss on 2016/11/1.
 */
public class ItemUserInfoModel extends BaseObservable {

    private ItemUserinfoBinding itemUserinfoBinding;

    public ItemUserInfoModel(ItemUserinfoBinding itemUserinfoBinding) {
        this.itemUserinfoBinding = itemUserinfoBinding;
    }
}
