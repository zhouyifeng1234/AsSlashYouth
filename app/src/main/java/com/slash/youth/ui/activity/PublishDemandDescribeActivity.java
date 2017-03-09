package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishDemandDescModel;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescribeActivity extends BaseActivity {

    private ActivityPublishDemandDescribeBinding mActivityPublishDemandDescribeBinding;
    private PublishDemandDescModel mPublishDemandDescModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle publishDemandDataBundle = getIntent().getBundleExtra("publishDemandDataBundle");

        mActivityPublishDemandDescribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_describe);
        mPublishDemandDescModel = new PublishDemandDescModel(mActivityPublishDemandDescribeBinding, this, publishDemandDataBundle);
        mActivityPublishDemandDescribeBinding.setPublishDemandDescModel(mPublishDemandDescModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPublishDemandDescModel.mSaplAddPic.addPicResult(requestCode, resultCode, data);
    }
}
