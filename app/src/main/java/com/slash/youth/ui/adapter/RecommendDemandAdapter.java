package com.slash.youth.ui.adapter;

import com.slash.youth.domain.RecommendDemandUserBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.RecommendDemandHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class RecommendDemandAdapter extends SlashBaseAdapter<RecommendDemandUserBean.DemandUserInfo> {
    public static ArrayList<Integer> listCheckedItemId = new ArrayList<Integer>();

    public RecommendDemandAdapter(ArrayList<RecommendDemandUserBean.DemandUserInfo> listData) {
        super(listData);
    }

    @Override
    public ArrayList<RecommendDemandUserBean.DemandUserInfo> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new RecommendDemandHolder();
    }
}
