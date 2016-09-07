package com.slash.youth.ui.adapter;

import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.LocationCitySearchListHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class LocationCitySearchListAdapter extends SlashBaseAdapter<String> {
    public LocationCitySearchListAdapter(ArrayList<String> listData) {
        super(listData);
    }

    @Override
    public ArrayList<String> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new LocationCitySearchListHolder();
    }
}
