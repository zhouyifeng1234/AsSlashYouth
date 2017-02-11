package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.ui.viewmodel.PagerHomeContactsModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeContactsPager extends BaseHomePager {

    private PagerHomeContactsModel pagerHomeContactsModel;
    public HomeContactsPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        ActivityHomeModel activityHomeModel = HomeActivity.activityHomeModel;
        PagerHomeContactsBinding pagerHomeContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_contacts, null, false);
        pagerHomeContactsModel = new PagerHomeContactsModel(pagerHomeContactsBinding, mActivity,activityHomeModel);
        pagerHomeContactsBinding.setPagerHomeContactsModel(pagerHomeContactsModel);
        return pagerHomeContactsBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }

}
