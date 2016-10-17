package com.slash.youth.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityFirstLetterHolder extends BaseHolder<Character> {

    private TextView mTvLocationCityFirstLetter;

    @Override
    public View initView() {
        mTvLocationCityFirstLetter = new TextView(CommonUtils.getContext());
        mTvLocationCityFirstLetter.setTextSize((float) 10.5);
        mTvLocationCityFirstLetter.setTextColor(Color.parseColor("#999999"));
        return mTvLocationCityFirstLetter;
    }

    @Override
    public void refreshView(Character data) {
        mTvLocationCityFirstLetter.setText(data + "");
    }
}
