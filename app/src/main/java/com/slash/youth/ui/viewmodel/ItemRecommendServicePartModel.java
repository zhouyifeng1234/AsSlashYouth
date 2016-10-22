package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendServicePartBinding;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class ItemRecommendServicePartModel extends BaseObservable {
    ItemRecommendServicePartBinding mItemRecommendServicePartBinding;
    private int mCurrentPosition;

    public ItemRecommendServicePartModel(ItemRecommendServicePartBinding itemRecommendServicePartBinding) {
        this.mItemRecommendServicePartBinding = itemRecommendServicePartBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    public void checkRecommendServicePart(View v) {
        if (RecommendServicePartAdapter.listCheckedItemId.contains(mCurrentPosition)) {
            RecommendServicePartAdapter.listCheckedItemId.remove(new Integer(mCurrentPosition));
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.default_btn);
        } else {
            RecommendServicePartAdapter.listCheckedItemId.add(mCurrentPosition);
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.pitchon_btn);
        }
    }


    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }
}
