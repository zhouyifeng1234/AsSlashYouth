package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityCityLocationBinding;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class ActivityCityLocationModel extends BaseObservable {
    ActivityCityLocationBinding mActivityCityLocationBinding;

    public ActivityCityLocationModel(ActivityCityLocationBinding activityCityLocationBinding) {
        this.mActivityCityLocationBinding = activityCityLocationBinding;
    }

}
