package com.slash.youth.ui.adapter;

import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.RecordHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/6.
 */
public class RecordAdapter extends SlashBaseAdapter<TransactionRecoreBean.DataBean.ListBean> {

    public RecordAdapter(ArrayList<TransactionRecoreBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<TransactionRecoreBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new RecordHolder();
    }
}
