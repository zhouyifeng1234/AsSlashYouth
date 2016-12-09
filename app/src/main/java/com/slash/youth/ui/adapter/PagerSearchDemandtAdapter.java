package com.slash.youth.ui.adapter;

import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerHomeDemandHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerSearchDemandtAdapter extends SlashBaseAdapter<SearchItemDemandBean.DataBean.ListBean> {

    public PagerSearchDemandtAdapter(ArrayList<SearchItemDemandBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchItemDemandBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerHomeDemandHolder();
    }
}
