package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMyThridPartyBinding;

/**
 * Created by zss on 2016/11/4.
 */
public class ThirdPartyModel extends BaseObservable {
    private ActivityMyThridPartyBinding activityMyThridPartyBinding;

    public ThirdPartyModel(ActivityMyThridPartyBinding activityMyThridPartyBinding) {
        this.activityMyThridPartyBinding = activityMyThridPartyBinding;
    }
}
