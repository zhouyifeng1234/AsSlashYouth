package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBaseinfoBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishDemandBaseInfoModel;

/**
 * 发布需求页面 第二版
 * <p/>
 * Created by zhouyifeng on 2016/10/17.
 */
public class PublishDemandBaseInfoActivity extends BaseActivity {

    public static Activity mActivity;

    private PublishDemandBaseInfoModel mPublishDemandBaseInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityPublishDemandBaseinfoBinding activityPublishDemandBaseinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_baseinfo);
        mPublishDemandBaseInfoModel = new PublishDemandBaseInfoModel(activityPublishDemandBaseinfoBinding, this);
        activityPublishDemandBaseinfoBinding.setPublishDemandbaseInfoModel(mPublishDemandBaseInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPublishDemandBaseInfoModel.mSaplAddPic.addPicResult(requestCode, resultCode, data);
    }
}
