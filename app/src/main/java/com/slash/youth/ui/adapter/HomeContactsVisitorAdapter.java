package com.slash.youth.ui.adapter;

import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeContactsVisitorHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeContactsVisitorAdapter extends SlashBaseAdapter<HomeContactsVisitorBean.DataBean.ListBean> {
    public HomeContactsVisitorAdapter(ArrayList<HomeContactsVisitorBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<HomeContactsVisitorBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeContactsVisitorHolder();
    }
}
