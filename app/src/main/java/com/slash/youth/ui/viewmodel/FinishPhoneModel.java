package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityFinishPhoneBinding;

/**
 * Created by zss on 2016/11/2.
 */
    public class FinishPhoneModel extends BaseObservable {

    private ActivityFinishPhoneBinding activityFinishPhoneBinding;

    public FinishPhoneModel(ActivityFinishPhoneBinding activityFinishPhoneBinding) {
        this.activityFinishPhoneBinding = activityFinishPhoneBinding;
    }
}
