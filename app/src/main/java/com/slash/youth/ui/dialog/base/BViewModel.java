package com.slash.youth.ui.dialog.base;

import android.app.Activity;


/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/7/21
 */
public class BViewModel<T> {

    protected final Activity activity;

    public BViewModel() {
        activity = null;
    }

    public BViewModel(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    protected T binding;

    public void setBinding(T binding) {
        this.binding = binding;
    }

    protected void onStart() {

    }

    protected void onResume() {

    }

    protected void onPause() {

    }

    protected void onStop() {

    }

    protected void onDestroy() {

    }
}
