package com.slash.youth.ui.adapter;

import com.slash.youth.domain.AutoRecommendServicePartBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.RecommendServicePartHoler;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class RecommendServicePartAdapter extends SlashBaseAdapter<AutoRecommendServicePartBean> {
    public RecommendServicePartAdapter(ArrayList<AutoRecommendServicePartBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<AutoRecommendServicePartBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new RecommendServicePartHoler();
    }
}
