package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityResultAllBinding;

/**
 * Created by zss on 2016/10/16.
 */
public class SearchResultAllModel extends BaseObservable {

    private ActivityResultAllBinding activityResultAllBinding;

    public SearchResultAllModel(ActivityResultAllBinding activityResultAllBinding) {
        this.activityResultAllBinding = activityResultAllBinding;
    }
}
