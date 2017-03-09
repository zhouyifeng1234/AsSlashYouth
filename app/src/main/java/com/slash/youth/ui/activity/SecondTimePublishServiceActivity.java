package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.Activity2ndPublishServiceBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishService2ndModel;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class SecondTimePublishServiceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity2ndPublishServiceBinding activity2ndPublishServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_2nd_publish_service);
        PublishService2ndModel publishService2ndModel = new PublishService2ndModel(activity2ndPublishServiceBinding,this);
        activity2ndPublishServiceBinding.setPublishService2ndModel(publishService2ndModel);

    }
}
