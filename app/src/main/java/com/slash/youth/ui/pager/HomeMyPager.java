package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeMyBinding;
import com.slash.youth.ui.viewmodel.PagerHomeMyModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeMyPager extends BaseHomePager {

    public HomeMyPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        PagerHomeMyBinding pagerHomeMyBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_my, null, false);
        PagerHomeMyModel pagerHomeMyModel = new PagerHomeMyModel(pagerHomeMyBinding, mActivity);
        pagerHomeMyBinding.setPagerHomeMyModel(pagerHomeMyModel);
        return pagerHomeMyBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }
}
