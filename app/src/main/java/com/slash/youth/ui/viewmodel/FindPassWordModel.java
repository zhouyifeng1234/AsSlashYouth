package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityFindPasswordBinding;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordModel extends BaseObservable {
    private ActivityFindPasswordBinding activityFindPasswordBinding;

    public FindPassWordModel(ActivityFindPasswordBinding activityFindPasswordBinding) {
        this.activityFindPasswordBinding = activityFindPasswordBinding;
    }
}
