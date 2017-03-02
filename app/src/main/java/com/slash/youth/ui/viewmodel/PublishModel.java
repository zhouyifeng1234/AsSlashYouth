package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishBinding;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2017/2/26.
 */
public class PublishModel extends BaseObservable {

    ActivityPublishBinding mActivityPublishBinding;
    Activity mActivity;

    public PublishModel(ActivityPublishBinding activityPublishBinding, Activity activity) {
        this.mActivityPublishBinding = activityPublishBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {

    }

    private void initListener() {

    }

    public void cancelPublish(View v) {
        mActivity.finish();
    }

    /**
     * 发布需求
     *
     * @param v
     */
    public void publishDemand(View v) {
        Intent intentPublishDemandBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
        mActivity.startActivity(intentPublishDemandBaseInfoActivity);
    }

    /**
     * 发布服务
     *
     * @param v
     */
    public void publishService(View v) {
        Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
        mActivity.startActivity(intentPublishServiceBaseInfoActivity);
    }
}
