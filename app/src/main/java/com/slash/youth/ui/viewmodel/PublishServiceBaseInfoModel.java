package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceBaseInfoModel extends BaseObservable {

    ActivityPublishServiceBaseinfoBinding mActivityPublishServiceBaseinfoBinding;
    Activity mActivity;

    public PublishServiceBaseInfoModel(ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceBaseinfoBinding = activityPublishServiceBaseinfoBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }


    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void checkRealName(View v){

    }

    public void checkAnonymous(View v){

    }

}
