package com.slash.youth.ui.adapter;

import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerMorwServiceHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMoreServiceAdapter extends SlashBaseAdapter<FreeTimeMoreServiceBean.DataBean.ListBean> {

    private FirstPagerMoreActivity firstPagerMoreActivity;
    public PagerMoreServiceAdapter(ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean> listData,FirstPagerMoreActivity firstPagerMoreActivity) {
        super(listData);
        this.firstPagerMoreActivity = firstPagerMoreActivity;
    }

    @Override
    public ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerMorwServiceHolder(firstPagerMoreActivity);
    }
}
