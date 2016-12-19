package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.ui.viewmodel.PagerHomeFreeTimeModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeFreeTimePager extends BaseHomePager {


    public HomeFreeTimePager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
       /* PagerHomeFreeTimeBinding pagerHomeFreeTimeBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_free_time, null, false);
        PagerHomeFreeTimeModel pagerHomeFreeTimeModel1 = new PagerHomeFreeTimeModel(pagerHomeFreeTimeBinding,mActivity);
        pagerHomeFreeTimeBinding.setPagerHomeFreeTimeModel(pagerHomeFreeTimeModel1);*/

        PagerHomeFreetimeBinding pagerHomeFreetimeBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_freetime, null, false);
        PagerHomeFreeTimeModel pagerHomeFreeTimeModel = new PagerHomeFreeTimeModel(pagerHomeFreetimeBinding, mActivity);
        pagerHomeFreetimeBinding.setPagerHomeFreeTimeModel(pagerHomeFreeTimeModel);
        return pagerHomeFreetimeBinding.getRoot();

       /* PagerHomeFreeTimeBinding  pagerHomeFreeTimeinflateViewBing = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_free_time, null, false);
        PagerHomeFreeTimeModel pagerHomeFreeTimeModel = new PagerHomeFreeTimeModel(pagerHomeFreeTimeinflateViewBing, mActivity);
        pagerHomeFreeTimeinflateViewBing.setPagerHomeFreeTimeModel(pagerHomeFreeTimeModel);*/

    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }
}
