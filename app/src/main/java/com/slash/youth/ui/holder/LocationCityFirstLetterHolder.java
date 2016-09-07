package com.slash.youth.ui.holder;

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
        mTvLocationCityFirstLetter.setTextSize(12);
        return mTvLocationCityFirstLetter;
    }

    @Override
    public void refreshView(Character data) {
        mTvLocationCityFirstLetter.setText(data + "");
    }
}
