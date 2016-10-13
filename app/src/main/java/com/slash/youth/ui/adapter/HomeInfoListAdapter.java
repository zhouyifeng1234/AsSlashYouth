package com.slash.youth.ui.adapter;

import com.slash.youth.domain.HomeInfoBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeInfoListHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class HomeInfoListAdapter extends SlashBaseAdapter<HomeInfoBean> {

    public HomeInfoListAdapter(ArrayList<HomeInfoBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<HomeInfoBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeInfoListHolder();
    }
}
