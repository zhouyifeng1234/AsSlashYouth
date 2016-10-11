package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.PagerHomeMyBinding;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeMyModel extends BaseObservable {
    PagerHomeMyBinding mPagerHomeMyBinding;
    Activity mActivity;

    public PagerHomeMyModel(PagerHomeMyBinding pagerHomeMyBinding, Activity activity) {
        this.mPagerHomeMyBinding = pagerHomeMyBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {

    }
}
