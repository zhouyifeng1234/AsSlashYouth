package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMyPublishServiceBinding;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyPublishServiceModel extends BaseObservable {

    ActivityMyPublishServiceBinding mActivityMyPublishServiceBinding;
    Activity mActivity;

    public MyPublishServiceModel(ActivityMyPublishServiceBinding activityMyPublishServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyPublishServiceBinding = activityMyPublishServiceBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
