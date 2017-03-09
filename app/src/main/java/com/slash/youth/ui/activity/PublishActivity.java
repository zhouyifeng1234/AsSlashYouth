package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishModel;

/**
 * V1.1版 发布页面
 * <p/>
 * Created by zhouyifeng on 2017/2/26.
 */
public class PublishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishBinding activityPublishBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish);
        PublishModel publishModel = new PublishModel(activityPublishBinding, this);
        activityPublishBinding.setPublishModel(publishModel);
    }
}
