package com.slash.youth.ui.adapter;

import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.LocationCityInfoHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityInfoAdapter extends SlashBaseAdapter<LocationCityInfo> {

    public LocationCityInfoAdapter(ArrayList<LocationCityInfo> listData) {
        super(listData);
    }

    @Override
    public ArrayList<LocationCityInfo> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new LocationCityInfoHolder();
    }
}
