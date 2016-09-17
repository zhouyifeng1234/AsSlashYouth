package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityPublishDemandBinding;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PublishDemandModel extends BaseObservable {
    ActivityPublishDemandBinding mActivityPublishDemandBinding;

    public PublishDemandModel(ActivityPublishDemandBinding activityPublishDemandBinding) {
        this.mActivityPublishDemandBinding = activityPublishDemandBinding;
    }
}
