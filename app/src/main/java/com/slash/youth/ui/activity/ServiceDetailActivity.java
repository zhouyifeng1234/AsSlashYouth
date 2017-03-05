package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityServiceDetailBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ServiceDetailModel;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ServiceDetailActivity extends BaseActivity {

    private ServiceDetailModel mServiceDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityServiceDetailBinding activityServiceDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_service_detail);
        mServiceDetailModel = new ServiceDetailModel(activityServiceDetailBinding, this);
        activityServiceDetailBinding.setServiceDetailModel(mServiceDetailModel);
    }

    @Override
    public void onBackPressed() {
        int viewPicVisibility = mServiceDetailModel.getViewPicVisibility();
        if (viewPicVisibility == View.VISIBLE) {
            mServiceDetailModel.setViewPicVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
