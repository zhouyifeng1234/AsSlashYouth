package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyBidDemandBinding;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyBidDemandModel extends BaseObservable {

    ActivityMyBidDemandBinding mActivityMyBidDemandBinding;
    Activity mActivity;

    public MyBidDemandModel(ActivityMyBidDemandBinding activityMyBidDemandBinding, Activity activity) {
        this.mActivityMyBidDemandBinding = activityMyBidDemandBinding;
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
