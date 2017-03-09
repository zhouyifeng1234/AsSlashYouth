package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ItemListviewHomeContactsVisitorBinding;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeContactsVisitorModel extends BaseObservable {
    ItemListviewHomeContactsVisitorBinding mItemListviewHomeContactsVisitorBinding;

    public HomeContactsVisitorModel(ItemListviewHomeContactsVisitorBinding itemListviewHomeContactsVisitorBinding) {
        this.mItemListviewHomeContactsVisitorBinding = itemListviewHomeContactsVisitorBinding;
        initView();
    }

    private void initView() {
    }
}
