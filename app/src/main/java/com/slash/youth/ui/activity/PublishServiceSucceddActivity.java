package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceSuccessBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishServiceSuccessModel;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceSucceddActivity extends BaseActivity {
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        ActivityPublishServiceSuccessBinding activityPublishServiceSuccessBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_success);
        PublishServiceSuccessModel publishServiceSuccessModel = new PublishServiceSuccessModel(activityPublishServiceSuccessBinding, this);
        activityPublishServiceSuccessBinding.setPublishServiceSuccessModel(publishServiceSuccessModel);

    }
}
