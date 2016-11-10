package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemServiceDetailRecommendServiceBinding;
import com.slash.youth.domain.SimilarServiceRecommendBean;

/**
 * Created by zhouyifeng on 2016/11/10.
 */
public class ItemServiceDetailRecommendServiceModel extends BaseObservable {

    ItemServiceDetailRecommendServiceBinding mItemServiceDetailRecommendServiceBinding;
    Activity mActivity;
    SimilarServiceRecommendBean mSimilarServiceRecommendBean;

    public ItemServiceDetailRecommendServiceModel(ItemServiceDetailRecommendServiceBinding itemServiceDetailRecommendServiceBinding, Activity activity, SimilarServiceRecommendBean similarServiceRecommendBean) {
        this.mActivity = activity;
        this.mItemServiceDetailRecommendServiceBinding = itemServiceDetailRecommendServiceBinding;
        this.mSimilarServiceRecommendBean = similarServiceRecommendBean;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
