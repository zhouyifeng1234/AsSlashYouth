package com.slash.youth.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.LocationCityFirstLetterHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityFirstLetterAdapter extends SlashBaseAdapter<Character> {
    public LocationCityFirstLetterAdapter(ArrayList<Character> listData) {
        super(listData);
    }

    @Override
    public ArrayList<Character> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new LocationCityFirstLetterHolder();
    }
}
