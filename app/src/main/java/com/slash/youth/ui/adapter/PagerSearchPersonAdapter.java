package com.slash.youth.ui.adapter;

import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchPersonBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerHomeDemandHolder;
import com.slash.youth.ui.holder.PagerSearchPersonHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchPersonAdapter extends SlashBaseAdapter<SearchPersonBean> {

    public PagerSearchPersonAdapter(ArrayList<SearchPersonBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchPersonBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerSearchPersonHolder();
    }
}
