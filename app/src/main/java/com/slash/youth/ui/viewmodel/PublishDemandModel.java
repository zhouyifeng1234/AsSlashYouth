package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishDemandBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PublishDemandModel extends BaseObservable {
    ActivityPublishDemandBinding mActivityPublishDemandBinding;

    public PublishDemandModel(ActivityPublishDemandBinding activityPublishDemandBinding) {
        this.mActivityPublishDemandBinding = activityPublishDemandBinding;
    }

    public void getDetailLocation(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMapActivity);
    }
}
