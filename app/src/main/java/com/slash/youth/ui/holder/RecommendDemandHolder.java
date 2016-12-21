package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendDemandBinding;
import com.slash.youth.domain.RecommendDemandUserBean;
import com.slash.youth.ui.adapter.RecommendDemandAdapter;
import com.slash.youth.ui.viewmodel.ItemRecommendDemandModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class RecommendDemandHolder extends BaseHolder<RecommendDemandUserBean.DemandUserInfo> {

    private ItemRecommendDemandBinding mItemRecommendDemandBinding;
    private ItemRecommendDemandModel mItemRecommendDemandModel;

    @Override
    public View initView() {
        mItemRecommendDemandBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_recommend_demand, null, false);
        mItemRecommendDemandModel = new ItemRecommendDemandModel(mItemRecommendDemandBinding);
        mItemRecommendDemandBinding.setItemRecommendDemandModel(mItemRecommendDemandModel);
        return mItemRecommendDemandBinding.getRoot();
    }

    @Override
    public void refreshView(RecommendDemandUserBean.DemandUserInfo data) {
        mItemRecommendDemandModel.setCurrentPosition(getCurrentPosition());
        if (RecommendDemandAdapter.listCheckedItemId.contains(getCurrentPosition())) {
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.pitchon_btn);
        } else {
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.default_btn);
        }

        BitmapKit.bindImage(mItemRecommendDemandBinding.ivDemandUserAvatar, data.avatar);
        if (data.isauth == 0) {
            //未认证
            mItemRecommendDemandModel.setAuthVisibility(View.GONE);
        } else {
            //已认证
            mItemRecommendDemandModel.setAuthVisibility(View.VISIBLE);
        }
        mItemRecommendDemandModel.setDemandUsername(data.name);
    }
}
