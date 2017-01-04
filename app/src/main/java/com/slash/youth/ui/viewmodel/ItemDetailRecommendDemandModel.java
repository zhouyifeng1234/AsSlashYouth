package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemDetailRecommendDemandBinding;
import com.slash.youth.domain.DetailRecommendDemandList;

/**
 * Created by zhouyifeng on 2017/1/4.
 */
public class ItemDetailRecommendDemandModel extends BaseObservable {
    ItemDetailRecommendDemandBinding mItemDetailRecommendDemandBinding;
    Activity mActivity;
    DetailRecommendDemandList.RecommendDemandInfo mRecommendDemandInfo;

    public ItemDetailRecommendDemandModel(ItemDetailRecommendDemandBinding itemDetailRecommendDemandBinding, Activity activity, DetailRecommendDemandList.RecommendDemandInfo recommendDemandInfo) {
        this.mItemDetailRecommendDemandBinding = itemDetailRecommendDemandBinding;
        this.mActivity = activity;
        this.mRecommendDemandInfo = recommendDemandInfo;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
    }
}
