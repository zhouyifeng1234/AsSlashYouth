package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityDemandDetailBinding;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailModel extends BaseObservable {

    ActivityDemandDetailBinding mActivityDemandDetailBinding;
    Activity mActivity;

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
    }
}
