package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPaymentBinding;
import com.slash.youth.ui.viewmodel.PaymentModel;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class PaymentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPaymentBinding activityPaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        PaymentModel paymentModel = new PaymentModel(activityPaymentBinding, this);
        activityPaymentBinding.setPaymentModel(paymentModel);
    }
}
