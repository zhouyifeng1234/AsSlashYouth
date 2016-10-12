package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.domain.HomeDemandAndServiceBean;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.HomeDemandAndServiceAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

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
        initData();
    }

    ArrayList<HomeDemandAndServiceBean> listDemandServiceBean = new ArrayList<HomeDemandAndServiceBean>();

    private void initView() {
        getDataFromServer();
        mPagerHomeFreetimeBinding.lvHomeDemandAndService.setAdapter(new HomeDemandAndServiceAdapter(listDemandServiceBean));
    }

    private void initData() {

    }

    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        intentSearchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSearchActivity);
    }

    public void getDataFromServer() {
        //模拟数据 首页闲时 需求、服务列表
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
        listDemandServiceBean.add(new HomeDemandAndServiceBean());
    }

}
