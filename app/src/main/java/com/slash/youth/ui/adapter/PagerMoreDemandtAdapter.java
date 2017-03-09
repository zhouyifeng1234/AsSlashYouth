package com.slash.youth.ui.adapter;

import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerHomeDemandHolder;
import com.slash.youth.ui.holder.PagerMoreDemandHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMoreDemandtAdapter extends SlashBaseAdapter<FreeTimeMoreDemandBean.DataBean.ListBean> {
    private FirstPagerMoreActivity firstPagerMoreActivity;

    public PagerMoreDemandtAdapter(ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean> listData,FirstPagerMoreActivity firstPagerMoreActivity) {
        super(listData);
        this.firstPagerMoreActivity = firstPagerMoreActivity;
    }

    @Override
    public ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerMoreDemandHolder(firstPagerMoreActivity);
    }
}
