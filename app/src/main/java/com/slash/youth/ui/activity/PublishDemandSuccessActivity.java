package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandSuccessBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishDemandSuccessModel;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandSuccessActivity extends BaseActivity {
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityPublishDemandSuccessBinding activityPublishDemandSuccessBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_success);
        PublishDemandSuccessModel publishDemandSuccessModel = new PublishDemandSuccessModel(activityPublishDemandSuccessBinding, this);
        activityPublishDemandSuccessBinding.setPublishDemandSuccessModel(publishDemandSuccessModel);
    }
}
