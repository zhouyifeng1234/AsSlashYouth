package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityRefundBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.RefundModel;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class RefundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRefundBinding activityRefundBinding = DataBindingUtil.setContentView(this, R.layout.activity_refund);
        RefundModel refundModel = new RefundModel(activityRefundBinding, this);
        activityRefundBinding.setRefundModel(refundModel);
    }
}
