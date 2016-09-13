package com.slash.youth.ui.adapter;

import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MapNearLocationHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class MapNearLocationAdapter extends SlashBaseAdapter<NearLocationBean> {
    public MapNearLocationAdapter(ArrayList<NearLocationBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<NearLocationBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new MapNearLocationHolder();
    }
}
