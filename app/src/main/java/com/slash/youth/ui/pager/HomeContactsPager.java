package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.ui.viewmodel.PagerHomeContactsModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeContactsPager extends BaseHomePager {
    public HomeContactsPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
//        TextView tv = new TextView(CommonUtils.getContext());
//        tv.setText("HomeContactsPager");
//        return tv;

        PagerHomeContactsBinding pagerHomeContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_contacts, null, false);
        PagerHomeContactsModel pagerHomeContactsModel = new PagerHomeContactsModel(pagerHomeContactsBinding, mActivity);
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
