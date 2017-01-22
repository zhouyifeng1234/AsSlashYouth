package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityServiceDetailBinding;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ServiceDetailModel;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ServiceDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityServiceDetailBinding activityServiceDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_service_detail);
        ServiceDetailModel serviceDetailModel = new ServiceDetailModel(activityServiceDetailBinding, this);
        activityServiceDetailBinding.setServiceDetailModel(serviceDetailModel);
    }
}
