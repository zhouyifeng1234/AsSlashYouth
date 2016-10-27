package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyPublishDemandBinding;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyPublishDemandModel extends BaseObservable {

    ActivityMyPublishDemandBinding mActivityMyPublishDemandBinding;
    Activity mActivity;

    public MyPublishDemandModel(ActivityMyPublishDemandBinding activityMyPublishDemandBinding, Activity activity) {
        this.mActivityMyPublishDemandBinding = activityMyPublishDemandBinding;
        this.mActivity = activity;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

}
