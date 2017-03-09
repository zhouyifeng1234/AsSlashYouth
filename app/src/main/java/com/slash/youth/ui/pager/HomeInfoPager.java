package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeInfoBinding;
import com.slash.youth.ui.viewmodel.PagerHomeInfoModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeInfoPager extends BaseHomePager {

    public PagerHomeInfoModel mPagerHomeInfoModel;

    public HomeInfoPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        PagerHomeInfoBinding pagerHomeInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_info, null, false);
        mPagerHomeInfoModel = new PagerHomeInfoModel(pagerHomeInfoBinding, mActivity);
        pagerHomeInfoBinding.setPagerHomeInfoModel(mPagerHomeInfoModel);
        return pagerHomeInfoBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }
}
