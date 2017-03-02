package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeWorkbenchBinding;
import com.slash.youth.ui.viewmodel.PagerHomeWorkbenchModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class HomeWorkbenchPager extends BaseHomePager {
    public HomeWorkbenchPager(Activity activity) {
        super(activity);
    }

    public HomeWorkbenchPager(Activity activity, Runnable runnable) {
        super(activity, runnable);
    }

    public PagerHomeWorkbenchModel mPagerHomeWorkbenchModel;

    @Override
    public View initView() {
        PagerHomeWorkbenchBinding pagerHomeWorkbenchBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_workbench, null, false);
        mPagerHomeWorkbenchModel = new PagerHomeWorkbenchModel(pagerHomeWorkbenchBinding, mActivity);
        pagerHomeWorkbenchBinding.setPagerHomeWorkbenchModel(mPagerHomeWorkbenchModel);
        return pagerHomeWorkbenchBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }
}
