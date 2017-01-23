package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.ui.viewmodel.MyPublishDemandModel;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyPublishDemandActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyPublishDemandBinding activityMyPublishDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_publish_demand);
        MyPublishDemandModel myPublishDemandModel = new MyPublishDemandModel(activityMyPublishDemandBinding, this);
        activityMyPublishDemandBinding.setMyPublishDemandModel(myPublishDemandModel);
    }
}
