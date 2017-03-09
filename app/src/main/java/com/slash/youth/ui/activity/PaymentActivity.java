package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.pingplusplus.android.Pingpp;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPaymentBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PaymentModel;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class PaymentActivity extends BaseActivity {

    private PaymentModel mPaymentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPaymentBinding activityPaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        mPaymentModel = new PaymentModel(activityPaymentBinding, this);
        activityPaymentBinding.setPaymentModel(mPaymentModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                for (String key : data.getExtras().keySet()) {
                    LogKit.v("----------------" + key + ":" + data.getExtras().get(key) + "----------------");
                }

                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）

                if ("success".equals(result)) {
                    //第三方支付成功
                    mPaymentModel.doPaySuccess();
                } else if ("fail".equals(result)) {
                    //第三方支付失败
                    mPaymentModel.doThirdPayFail();
                } else if ("cancel".equals(result)) {
                    //取消支付
                    ToastUtils.shortToast("已取消第三方支付");
                } else if ("invalid".equals(result)) {
                    ToastUtils.shortToast("支付插件未安装（一般是微信客户端未安装的情况）");
                }

//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
                LogKit.v("payment result:" + result);
            }
        }
    }
}
