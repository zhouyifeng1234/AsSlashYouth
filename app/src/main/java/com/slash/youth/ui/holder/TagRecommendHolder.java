package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemTagRecommendBinding;
import com.slash.youth.domain.ItemTagRecommendModel;
import com.slash.youth.domain.TagRecommendList;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2017/3/2.
 */
public class TagRecommendHolder extends BaseHolder<TagRecommendList.TagRecommendInfo> {

    ItemTagRecommendModel mItemTagRecommendModel;

    @Override
    public View initView() {
        ItemTagRecommendBinding itemTagRecommendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_tag_recommend, null, false);
        mItemTagRecommendModel = new ItemTagRecommendModel(itemTagRecommendBinding);
        itemTagRecommendBinding.setItemTagRecommendModel(mItemTagRecommendModel);
        return itemTagRecommendBinding.getRoot();
    }

    @Override
    public void refreshView(TagRecommendList.TagRecommendInfo data) {
        mItemTagRecommendModel.setData(data);
    }
}
