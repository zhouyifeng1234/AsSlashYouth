package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendServicePartBinding;
import com.slash.youth.domain.AutoRecommendServicePartBean;
import com.slash.youth.ui.viewmodel.ItemRecommendServicePartModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class RecommendServicePartHoler extends BaseHolder<AutoRecommendServicePartBean> {

    private ItemRecommendServicePartModel mItemRecommendServicePartModel;

    @Override
    public View initView() {
        ItemRecommendServicePartBinding itemRecommendServicePartBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_recommend_service_part, null, false);
        mItemRecommendServicePartModel = new ItemRecommendServicePartModel(itemRecommendServicePartBinding);
        itemRecommendServicePartBinding.setItemRecommendServicePartModel(mItemRecommendServicePartModel);
        return itemRecommendServicePartBinding.getRoot();
    }

    @Override
    public void refreshView(AutoRecommendServicePartBean data) {

    }

}
