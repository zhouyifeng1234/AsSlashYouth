package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishServiceSuccessBinding;
import com.slash.youth.domain.AutoRecommendDemandBean;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.RecommendDemandAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceSuccessModel extends BaseObservable {

    ActivityPublishServiceSuccessBinding mActivityPublishServiceSuccessBinding;
    Activity mActivity;
    long serviceId;


    public PublishServiceSuccessModel(ActivityPublishServiceSuccessBinding activityPublishServiceSuccessBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceSuccessBinding = activityPublishServiceSuccessBinding;
        initData();
        initView();
    }

    ArrayList<AutoRecommendDemandBean> listRecommendDemand = new ArrayList<AutoRecommendDemandBean>();

    private void initData() {
        serviceId = mActivity.getIntent().getLongExtra("serviceId", -1);
        getRecommendDemandData();
    }

    private void initView() {

    }

    public void closeSuccessActivity(View v) {
        mActivity.finish();
    }

    //跳转到服务详情页
    public void gotoServiceDetail(View v) {
        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
        intentServiceDetailActivity.putExtra("serviceId", serviceId);
        mActivity.startActivity(intentServiceDetailActivity);
    }

    public void getRecommendDemandData() {
        //模拟推荐需求数据
        listRecommendDemand.add(new AutoRecommendDemandBean());
        listRecommendDemand.add(new AutoRecommendDemandBean());
        listRecommendDemand.add(new AutoRecommendDemandBean());
        listRecommendDemand.add(new AutoRecommendDemandBean());
        listRecommendDemand.add(new AutoRecommendDemandBean());
        listRecommendDemand.add(new AutoRecommendDemandBean());

        mActivityPublishServiceSuccessBinding.lvRecommendDemand.setAdapter(new RecommendDemandAdapter(listRecommendDemand));
    }
}
