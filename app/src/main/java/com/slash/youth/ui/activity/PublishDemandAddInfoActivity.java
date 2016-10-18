package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.ui.viewmodel.PublishDemandAddInfoModel;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_addinfo);
        PublishDemandAddInfoModel publishDemandAddInfoModel = new PublishDemandAddInfoModel(activityPublishDemandAddinfoBinding, this);
        activityPublishDemandAddinfoBinding.setPublishDemandAddInfoModel(publishDemandAddInfoModel);
    }
}
