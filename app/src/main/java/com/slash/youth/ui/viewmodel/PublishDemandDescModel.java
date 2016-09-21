package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.activity.PublishDemandModeActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescModel extends BaseObservable {
    ActivityPublishDemandDescribeBinding mActivityPublishDemandDescribeBinding;
    Activity mActivity;

    public PublishDemandDescModel(ActivityPublishDemandDescribeBinding activityPublishDemandDescribeBinding, Activity activity) {
        this.mActivityPublishDemandDescribeBinding = activityPublishDemandDescribeBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {


    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishDemandModeActivity = new Intent(CommonUtils.getContext(), PublishDemandModeActivity.class);
        intentPublishDemandModeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandModeActivity);
    }

}
