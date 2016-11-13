package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPaymentBinding;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class PaymentModel extends BaseObservable {

    ActivityPaymentBinding mActivityPaymentBinding;
    Activity mActivity;

    public PaymentModel(ActivityPaymentBinding activityPaymentBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPaymentBinding = activityPaymentBinding;
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

    //去支付
    public void payment(View v) {

    }

    public void closePaymentActivity(View v) {
        mActivity.finish();
    }

    //跳转到设置交易密码的界面（或者就在当前界面设置交易密码？）
    public void gotoSettingTransactionPassword(View v) {

    }


}
