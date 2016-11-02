package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.FloatViewBinding;

/**
 * Created by zss on 2016/11/1.
 */
public class FloatViewModel extends BaseObservable {

    private FloatViewBinding floatViewBinding;


    public FloatViewModel(FloatViewBinding floatViewBinding) {
        this.floatViewBinding = floatViewBinding;
    }
}
