package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBinding;
import com.slash.youth.ui.viewmodel.PublishDemandModel;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PublishDemandActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishDemandBinding activityPublishDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand);
        PublishDemandModel publishDemandModel = new PublishDemandModel(activityPublishDemandBinding,this);
        activityPublishDemandBinding.setPublishDemandModel(publishDemandModel);
    }

}
