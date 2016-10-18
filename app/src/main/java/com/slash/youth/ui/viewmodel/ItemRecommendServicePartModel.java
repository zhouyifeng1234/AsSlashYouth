package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemRecommendServicePartBinding;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class ItemRecommendServicePartModel extends BaseObservable {
    ItemRecommendServicePartBinding mItemRecommendServicePartBinding;

    public ItemRecommendServicePartModel(ItemRecommendServicePartBinding itemRecommendServicePartBinding) {
        this.mItemRecommendServicePartBinding = itemRecommendServicePartBinding;
    }
}
