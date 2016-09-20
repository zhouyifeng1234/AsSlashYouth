package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceInfoBinding;
import com.slash.youth.ui.viewmodel.PublishServiceInfoModel;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishServiceInfoBinding activityPublishServiceInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_info);
        PublishServiceInfoModel publishServiceInfoModel = new PublishServiceInfoModel(activityPublishServiceInfoBinding, this);
        activityPublishServiceInfoBinding.setPublishServiceInfoModel(publishServiceInfoModel);
    }
}
