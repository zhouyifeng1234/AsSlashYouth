package com.slash.youth.ui.adapter;

import android.widget.BaseAdapter;

import com.slash.youth.domain.RecordBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.RecordHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/6.
 */
public class RecordAdapter extends SlashBaseAdapter<RecordBean> {

    public RecordAdapter(ArrayList<RecordBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<RecordBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new RecordHolder();
    }
}
