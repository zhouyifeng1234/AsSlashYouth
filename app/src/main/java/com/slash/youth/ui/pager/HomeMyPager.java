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

    private PagerHomeMyModel pagerHomeMyModel;

    public HomeMyPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        PagerHomeMyBinding pagerHomeMyBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.pager_home_my, null, false);
        pagerHomeMyBinding.svPagerHomeMy.setHightView(pagerHomeMyBinding.rlHead, pagerHomeMyBinding.rlHeadUp, pagerHomeMyBinding.ivMine);
        pagerHomeMyModel = new PagerHomeMyModel(pagerHomeMyBinding, mActivity);
        pagerHomeMyBinding.setPagerHomeMyModel(pagerHomeMyModel);
        return pagerHomeMyBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }


    public void updateMessage() {
        pagerHomeMyModel.updateMessage();

    }

    public void doMarksAnimation() {
        pagerHomeMyModel.doMarksAnimation();
    }
}
