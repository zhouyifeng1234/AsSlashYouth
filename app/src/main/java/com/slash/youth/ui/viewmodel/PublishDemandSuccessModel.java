package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishDemandSuccessBinding;
import com.slash.youth.domain.AutoRecommendServicePartBean;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandSuccessModel extends BaseObservable {

    ActivityPublishDemandSuccessBinding mActivityPublishDemandSuccessBinding;
    Activity mActivity;
    private RecommendServicePartAdapter mRecommendServicePartAdapter;

    public PublishDemandSuccessModel(ActivityPublishDemandSuccessBinding activityPublishDemandSuccessBinding, Activity activity) {
        this.mActivityPublishDemandSuccessBinding = activityPublishDemandSuccessBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    ArrayList<AutoRecommendServicePartBean> listRecommendServicePart = new ArrayList<AutoRecommendServicePartBean>();

    private void initData() {
        getRecommendServicePartData();
    }

    private void initView() {
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVerticalScrollBarEnabled(false);
        mRecommendServicePartAdapter = new RecommendServicePartAdapter(listRecommendServicePart);
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setAdapter(mRecommendServicePartAdapter);
    }

    public void getRecommendServicePartData() {
        //模拟数据，系统自动推荐的优质服务方，实际应该由服务端接口返回(5到10个)
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
    }

    public void closeSuccessActivity(View v) {
        mActivity.finish();
    }

    public void gotoDemandDetail(View v) {
        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
        mActivity.startActivity(intentDemandDetailActivity);
    }
}
