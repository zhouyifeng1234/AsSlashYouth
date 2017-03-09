package com.slash.youth.ui.adapter;

import com.slash.youth.domain.PublishedServiceBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PublishedServiceHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishedServiceAdapter extends SlashBaseAdapter<PublishedServiceBean> {
    public PublishedServiceAdapter(ArrayList<PublishedServiceBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<PublishedServiceBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PublishedServiceHolder();
    }
}
