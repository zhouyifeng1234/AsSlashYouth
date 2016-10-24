package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendServicePartBinding;
import com.slash.youth.domain.AutoRecommendServicePartBean;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;
import com.slash.youth.ui.viewmodel.ItemRecommendServicePartModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class RecommendServicePartHoler extends BaseHolder<AutoRecommendServicePartBean> {
    private ItemRecommendServicePartBinding mItemRecommendServicePartBinding;
    private ItemRecommendServicePartModel mItemRecommendServicePartModel;


    @Override
    public View initView() {
        mItemRecommendServicePartBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_recommend_service_part, null, false);
        mItemRecommendServicePartModel = new ItemRecommendServicePartModel(mItemRecommendServicePartBinding);
        mItemRecommendServicePartBinding.setItemRecommendServicePartModel(mItemRecommendServicePartModel);
        return mItemRecommendServicePartBinding.getRoot();
    }

    @Override
    public void refreshView(AutoRecommendServicePartBean data) {
        mItemRecommendServicePartModel.setCurrentPosition(getCurrentPosition());
        if (RecommendServicePartAdapter.listCheckedItemId.contains(getCurrentPosition())) {
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.pitchon_btn);
        } else {
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.default_btn);
        }
    }

}
