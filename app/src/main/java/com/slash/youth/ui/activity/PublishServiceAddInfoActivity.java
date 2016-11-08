package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceAddinfoBinding;
import com.slash.youth.ui.viewmodel.PublishServiceAddInfoModel;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceAddInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishServiceAddinfoBinding activityPublishServiceAddinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_addinfo);
        PublishServiceAddInfoModel publishServiceAddInfoModel = new PublishServiceAddInfoModel(activityPublishServiceAddinfoBinding, this);
        activityPublishServiceAddinfoBinding.setPublishServiceAddInfoModel(publishServiceAddInfoModel);
    }
}
