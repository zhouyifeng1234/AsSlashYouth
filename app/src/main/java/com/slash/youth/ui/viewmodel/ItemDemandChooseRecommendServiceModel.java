package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemDemandChooseRecommendServiceBinding;
import com.slash.youth.domain.DemandChooseServiceBean;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class ItemDemandChooseRecommendServiceModel extends BaseObservable {

    ItemDemandChooseRecommendServiceBinding mItemDemandChooseRecommendServiceBinding;
    Activity mActivity;
    DemandChooseServiceBean mDemandChooseRecommendServiceBean;

    public ItemDemandChooseRecommendServiceModel(ItemDemandChooseRecommendServiceBinding itemDemandChooseRecommendServiceBinding, Activity activity, DemandChooseServiceBean demandChooseRecommendServiceBean) {
        this.mItemDemandChooseRecommendServiceBinding = itemDemandChooseRecommendServiceBinding;
        this.mActivity = activity;
        this.mDemandChooseRecommendServiceBean = demandChooseRecommendServiceBean;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
