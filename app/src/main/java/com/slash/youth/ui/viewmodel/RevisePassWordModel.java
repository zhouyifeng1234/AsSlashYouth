package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityRevisePasswordBinding;

/**
 * Created by zss on 2016/11/3.
 */
public class RevisePassWordModel extends BaseObservable {
    private ActivityRevisePasswordBinding activityRevisePasswordBinding;


    public RevisePassWordModel(ActivityRevisePasswordBinding activityRevisePasswordBinding) {
        this.activityRevisePasswordBinding = activityRevisePasswordBinding;
    }
}
