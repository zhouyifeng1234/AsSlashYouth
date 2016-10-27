package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemDemandChooseServiceBinding;
import com.slash.youth.domain.DemandChooseServiceBean;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class ItemDemandChooseServiceModel extends BaseObservable {

    ItemDemandChooseServiceBinding mItemDemandChooseServiceBinding;
    Activity mActivty;
    DemandChooseServiceBean mDemandChooseServiceBean;

    public ItemDemandChooseServiceModel(ItemDemandChooseServiceBinding itemDemandChooseServiceBinding, Activity activty, DemandChooseServiceBean demandChooseServiceBean) {
        this.mItemDemandChooseServiceBinding = itemDemandChooseServiceBinding;
        this.mActivty = activty;
        this.mDemandChooseServiceBean = demandChooseServiceBean;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
