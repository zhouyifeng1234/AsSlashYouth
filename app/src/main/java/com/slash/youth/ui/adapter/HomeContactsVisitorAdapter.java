package com.slash.youth.ui.adapter;

import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeContactsVisitorHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeContactsVisitorAdapter extends SlashBaseAdapter<HomeContactsVisitorBean> {
    public HomeContactsVisitorAdapter(ArrayList<HomeContactsVisitorBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<HomeContactsVisitorBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeContactsVisitorHolder();
    }
}
