package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishDemandModeBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandModeModel extends BaseObservable {

    ActivityPublishDemandModeBinding mActivityPublishDemandModeBinding;
    Activity mActivity;

    public PublishDemandModeModel(ActivityPublishDemandModeBinding activityPublishDemandModeBinding, Activity activity) {
        this.mActivityPublishDemandModeBinding = activityPublishDemandModeBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void publishDemand(View v) {
        ToastUtils.shortToast("发布需求成功");
    }

    public void getDetailLocation(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMapActivity);
    }
}
