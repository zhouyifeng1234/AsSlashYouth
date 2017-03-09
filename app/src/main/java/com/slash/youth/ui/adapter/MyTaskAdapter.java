package com.slash.youth.ui.adapter;

import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MyTaskHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskAdapter extends SlashBaseAdapter<MyTaskBean> {
    public MyTaskAdapter(ArrayList<MyTaskBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<MyTaskBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new MyTaskHolder();
    }
}
