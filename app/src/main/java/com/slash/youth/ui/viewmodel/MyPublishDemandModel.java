package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.utils.CommonUtils;

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

    //申请退款
    public void refund(View v) {
        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);
        mActivity.startActivity(intentRefundActivity);
    }

    //打开支付界面
    public void openPaymentActivity(View v) {
        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);
        mActivity.startActivity(intentPaymentActivity);
    }

}
