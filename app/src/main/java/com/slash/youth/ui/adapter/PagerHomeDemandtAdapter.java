package com.slash.youth.ui.adapter;

import com.slash.youth.domain.DemandBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerHomeDemandHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeDemandtAdapter extends SlashBaseAdapter<DemandBean> {

    public PagerHomeDemandtAdapter(ArrayList<DemandBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<DemandBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerHomeDemandHolder();
    }
}
