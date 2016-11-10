package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityServiceDetailBinding;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ServiceDetailModel extends BaseObservable {

    ActivityServiceDetailBinding mActivityServiceDetailBinding;
    Activity mActivity;

    public ServiceDetailModel(ActivityServiceDetailBinding activityServiceDetailBinding, Activity activity) {
        this.mActivityServiceDetailBinding = activityServiceDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
