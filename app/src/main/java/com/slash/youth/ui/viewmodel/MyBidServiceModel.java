package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyBidServiceBinding;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceModel extends BaseObservable {

    ActivityMyBidServiceBinding mActivityMyBidServiceBinding;
    Activity mActivity;

    public MyBidServiceModel(ActivityMyBidServiceBinding activityMyBidServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyBidServiceBinding = activityMyBidServiceBinding;
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


