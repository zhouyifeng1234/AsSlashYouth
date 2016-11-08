package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityPublishServiceAddinfoBinding;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceAddInfoModel extends BaseObservable {

    ActivityPublishServiceAddinfoBinding mActivityPublishServiceAddinfoBinding;
    Activity mActivity;

    public PublishServiceAddInfoModel(ActivityPublishServiceAddinfoBinding activityPublishServiceAddinfoBinding, Activity activity) {
        this.mActivityPublishServiceAddinfoBinding = activityPublishServiceAddinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }
}
