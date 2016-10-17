package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBaseinfoBinding;
import com.slash.youth.ui.viewmodel.PublishDemandBaseInfoModel;

/**
 * 发布需求页面 第二版
 * <p>
 * Created by zhouyifeng on 2016/10/17.
 */
public class PublishDemandBaseInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishDemandBaseinfoBinding activityPublishDemandBaseinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_baseinfo);
        PublishDemandBaseInfoModel publishDemandBaseInfoModel = new PublishDemandBaseInfoModel(activityPublishDemandBaseinfoBinding, this);
        activityPublishDemandBaseinfoBinding.setPublishDemandbaseInfoModel(publishDemandBaseInfoModel);
    }
}
