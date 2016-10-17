package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityPublishDemandBaseinfoBinding;

/**
 * 发布需求页面 第二版
 * Created by zhouyifeng on 2016/10/17.
 */
public class PublishDemandBaseInfoModel extends BaseObservable {
    ActivityPublishDemandBaseinfoBinding mActivityPublishDemandBaseinfoBinding;
    Activity mActivity;

    public PublishDemandBaseInfoModel(ActivityPublishDemandBaseinfoBinding activityPublishDemandBaseinfoBinding, Activity activity) {
        this.mActivityPublishDemandBaseinfoBinding = activityPublishDemandBaseinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }


}
