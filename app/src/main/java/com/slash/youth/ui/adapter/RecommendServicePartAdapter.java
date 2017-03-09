package com.slash.youth.ui.adapter;

import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.RecommendServicePartHoler;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class RecommendServicePartAdapter extends SlashBaseAdapter<RecommendServiceUserBean.ServiceUserInfo> {

    public static ArrayList<Integer> listCheckedItemId = new ArrayList<Integer>();

    public RecommendServicePartAdapter(ArrayList<RecommendServiceUserBean.ServiceUserInfo> listData) {
        super(listData);
    }

    @Override
    public ArrayList<RecommendServiceUserBean.ServiceUserInfo> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new RecommendServicePartHoler();
    }
}
