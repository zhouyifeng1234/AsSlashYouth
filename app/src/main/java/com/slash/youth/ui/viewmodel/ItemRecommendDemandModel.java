package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendDemandBinding;
import com.slash.youth.ui.adapter.RecommendDemandAdapter;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ItemRecommendDemandModel extends BaseObservable {
    ItemRecommendDemandBinding mItemRecommendDemandBinding;

    private int mCurrentPosition;

    public ItemRecommendDemandModel(ItemRecommendDemandBinding itemRecommendDemandBinding) {
        this.mItemRecommendDemandBinding = itemRecommendDemandBinding;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }


    public void checkRecommendDemand(View v) {
        if (RecommendDemandAdapter.listCheckedItemId.contains(mCurrentPosition)) {
            RecommendDemandAdapter.listCheckedItemId.remove(new Integer(mCurrentPosition));
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.default_btn);
        } else {
            RecommendDemandAdapter.listCheckedItemId.add(mCurrentPosition);
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.pitchon_btn);
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }
}
