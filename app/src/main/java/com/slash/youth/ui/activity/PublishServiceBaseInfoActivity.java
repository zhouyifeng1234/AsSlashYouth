package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;
import com.slash.youth.ui.viewmodel.PublishServiceBaseInfoModel;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceBaseInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_baseinfo);
        PublishServiceBaseInfoModel publishServiceBaseInfoModel = new PublishServiceBaseInfoModel(activityPublishServiceBaseinfoBinding, this);
        activityPublishServiceBaseinfoBinding.setPublishServiceBaseInfoModel(publishServiceBaseInfoModel);
    }
}
