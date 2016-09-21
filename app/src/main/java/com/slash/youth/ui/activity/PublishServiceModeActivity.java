package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceModeBinding;
import com.slash.youth.ui.viewmodel.PublishServiceModeModel;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceModeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishServiceModeBinding activityPublishServiceModeBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_mode);
        PublishServiceModeModel publishServiceModeModel = new PublishServiceModeModel(activityPublishServiceModeBinding, this);
        activityPublishServiceModeBinding.setPublishServiceModeModel(publishServiceModeModel);
    }
}
