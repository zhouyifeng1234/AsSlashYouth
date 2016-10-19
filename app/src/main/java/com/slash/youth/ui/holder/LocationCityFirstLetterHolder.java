package com.slash.youth.ui.holder;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class LocationCityFirstLetterHolder extends BaseHolder<Character> {

    private TextView mTvLocationCityFirstLetter;
    private int height;

    public int getHeight() {
        return height;
    }

    @Override
    public View initView() {
        mTvLocationCityFirstLetter = new TextView(CommonUtils.getContext());
        mTvLocationCityFirstLetter.setTextSize((float) 10.5);
        mTvLocationCityFirstLetter.setTextColor(Color.parseColor("#999999"));
        mTvLocationCityFirstLetter.setGravity(Gravity.CENTER);
        return mTvLocationCityFirstLetter;
    }


    @Override
    public void refreshView(Character data) {
        mTvLocationCityFirstLetter.setText(data + "");
        getTextHight(mTvLocationCityFirstLetter);

    }

    private int getTextHight(TextView mTvLocationCityFirstLetter) {
        mTvLocationCityFirstLetter.measure(0,0);
        height = mTvLocationCityFirstLetter.getMeasuredHeight();
        return height;
    }
}
