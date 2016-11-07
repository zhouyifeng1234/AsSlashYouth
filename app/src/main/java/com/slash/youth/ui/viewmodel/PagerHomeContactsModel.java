package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewHomeContactsBinding;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.adapter.HomeContactsVisitorAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

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
        initData();
    }

    ArrayList<HomeContactsVisitorBean> listHomeContactsVisitorBean = new ArrayList<HomeContactsVisitorBean>();

    private void initView() {
        HeaderListviewHomeContactsBinding headerListviewHomeContactsBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_home_contacts, null, false);
        HeaderHomeContactsModel headerHomeContactsModel = new HeaderHomeContactsModel(headerListviewHomeContactsBinding);
        headerListviewHomeContactsBinding.setHeaderHomeContactsModel(headerHomeContactsModel);
        View vContactsHeader = headerListviewHomeContactsBinding.getRoot();
        mPagerHomeContactsBinding.lvHomeContactsVisitor.addHeaderView(vContactsHeader);
    }

    private void initData() {
        getDataFromServer();
        //设置访客列表数据
        mPagerHomeContactsBinding.lvHomeContactsVisitor.setAdapter(new HomeContactsVisitorAdapter(listHomeContactsVisitorBean));
    }

    public void getDataFromServer() {
        //模拟数据 访客列表数据
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
        listHomeContactsVisitorBean.add(new HomeContactsVisitorBean());
    }



}
