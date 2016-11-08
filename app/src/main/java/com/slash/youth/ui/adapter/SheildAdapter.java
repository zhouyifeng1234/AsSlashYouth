package com.slash.youth.ui.adapter;

import android.widget.BaseAdapter;

import com.slash.youth.domain.SheildPersonBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SheildHolder;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class SheildAdapter extends SlashBaseAdapter<SheildPersonBean> {
    public SheildAdapter(ArrayList<SheildPersonBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SheildPersonBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SheildHolder();
    }
}
