package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.PagerHomeWorkbenchBinding;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class PagerHomeWorkbenchModel extends BaseObservable {

    PagerHomeWorkbenchBinding mPagerHomeWorkbenchBinding;
    Activity mActivity;

    public PagerHomeWorkbenchModel(PagerHomeWorkbenchBinding pagerHomeWorkbenchBinding, Activity activity) {
        this.mPagerHomeWorkbenchBinding = pagerHomeWorkbenchBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }
}
