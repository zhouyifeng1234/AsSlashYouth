package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityReplacePhoneBinding;

/**
 * Created by acer on 2016/11/2.
 */
public class ReplacePhoneModel extends BaseObservable {

    private ActivityReplacePhoneBinding activityReplacePhoneBinding;

    public ReplacePhoneModel(ActivityReplacePhoneBinding activityReplacePhoneBinding) {
        this.activityReplacePhoneBinding = activityReplacePhoneBinding;
    }
}
