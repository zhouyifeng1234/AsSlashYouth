package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.PagerHomeFreetimeBinding;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeFreeTimeModel extends BaseObservable {

    public Activity mActivity;
    PagerHomeFreetimeBinding mPagerHomeFreetimeBinding;

    public PagerHomeFreeTimeModel(PagerHomeFreetimeBinding pagerHomeFreetimeBinding, Activity activity) {
        this.mActivity = activity;
        this.mPagerHomeFreetimeBinding = pagerHomeFreetimeBinding;
        initView();

    }

    private void initView() {

    }


}
