package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.viewmodel.PublishDemandDescModel;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescribeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishDemandDescribeBinding activityPublishDemandDescribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_describe);
        PublishDemandDescModel publishDemandDescModel = new PublishDemandDescModel(activityPublishDemandDescribeBinding, this);
        activityPublishDemandDescribeBinding.setPublishDemandDescModel(publishDemandDescModel);
    }
}
