package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityDemandDetailLocationBinding;

/**
 * Created by zhouyifeng on 2016/10/25.
 */
public class DemandDetailLocationModel extends BaseObservable {

    ActivityDemandDetailLocationBinding mActivityDemandDetailLocationBinding;
    Activity mActivity;

    public DemandDetailLocationModel(ActivityDemandDetailLocationBinding activityDemandDetailLocationBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityDemandDetailLocationBinding = activityDemandDetailLocationBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

//    private String nearName;
//    private String nearAddress;
//    private String nearDistance;
//
//    @Bindable
//    public String getNearName() {
//        return nearName;
//    }
//
//    public void setNearName(String nearName) {
//        this.nearName = nearName;
//        notifyPropertyChanged(BR.nearName);
//    }
//
//    public String getNearAddress() {
//        return nearAddress;
//    }
//
//    @Bindable
//    public void setNearAddress(String nearAddress) {
//        this.nearAddress = nearAddress;
//        notifyPropertyChanged(BR.nearAddress);
//    }
//
//    @Bindable
//    public String getNearDistance() {
//        return nearDistance;
//    }
//
//    public void setNearDistance(String nearDistance) {
//        this.nearDistance = nearDistance;
//        notifyPropertyChanged(BR.nearDistance);
//    }
}
