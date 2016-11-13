package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityRefundBinding;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class RefundModel extends BaseObservable {

    ActivityRefundBinding mActivityRefundBinding;
    Activity mActivity;

    public RefundModel(ActivityRefundBinding activityRefundBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityRefundBinding = activityRefundBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

}
