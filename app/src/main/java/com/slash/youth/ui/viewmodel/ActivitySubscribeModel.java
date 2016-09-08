package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivitySubscribeBinding;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ActivitySubscribeModel extends BaseObservable {
    ActivitySubscribeBinding mActivitySubscribeBinding;

    public ActivitySubscribeModel(ActivitySubscribeBinding activitySubscribeBinding) {
        this.mActivitySubscribeBinding = activitySubscribeBinding;
    }

}
