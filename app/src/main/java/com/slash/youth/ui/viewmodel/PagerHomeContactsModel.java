package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.PagerHomeContactsBinding;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeContactsModel extends BaseObservable {
    PagerHomeContactsBinding mPagerHomeContactsBinding;
    Activity mActivity;

    public PagerHomeContactsModel(PagerHomeContactsBinding pagerHomeContactsBinding, Activity activity) {
        this.mPagerHomeContactsBinding = pagerHomeContactsBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {

    }
}
