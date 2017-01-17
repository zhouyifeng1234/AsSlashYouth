package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityPublishDemandSuccessBinding;
import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandSuccessModel extends BaseObservable {

    ActivityPublishDemandSuccessBinding mActivityPublishDemandSuccessBinding;
    Activity mActivity;
    long demandId;
    boolean isUpdate;

    public PublishDemandSuccessModel(ActivityPublishDemandSuccessBinding activityPublishDemandSuccessBinding, Activity activity) {
        this.mActivityPublishDemandSuccessBinding = activityPublishDemandSuccessBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        demandId = mActivity.getIntent().getLongExtra("demandId", -1);
        isUpdate = mActivity.getIntent().getBooleanExtra("isUpdate", false);
        getDataFromServer();
    }

    private void getDataFromServer() {
        DemandEngine.getRecommendServiceUser(new BaseProtocol.IResultExecutor<RecommendServiceUserBean>() {
            @Override
            public void execute(RecommendServiceUserBean dataBean) {
                mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setAdapter(new RecommendServicePartAdapter(dataBean.data.list));
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取推荐的服务者失败");
            }
        }, demandId + "", "5");
    }

    private void initView() {
        if (isUpdate) {
            setUpdateSuccessHintVisibility(View.VISIBLE);
            setPublishSuccessHintVisibility(View.GONE);
        } else {
            setUpdateSuccessHintVisibility(View.GONE);
            setPublishSuccessHintVisibility(View.VISIBLE);
        }
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVerticalScrollBarEnabled(false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("展示有效期至yyyy年MM月dd日HH:mm");
        String displayValidityDatetime = simpleDateFormat.format(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        mActivityPublishDemandSuccessBinding.tvDisplayValidityDatetime.setText(displayValidityDatetime);
    }

    public void closeSuccessActivity(View v) {
        mActivity.finish();
    }

    public void gotoDemandDetail(View v) {
        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
        intentDemandDetailActivity.putExtra("demandId", demandId);
        mActivity.startActivity(intentDemandDetailActivity);
    }

    private int publishSuccessHintVisibility;
    private int updateSuccessHintVisibility;

    @Bindable
    public int getUpdateSuccessHintVisibility() {
        return updateSuccessHintVisibility;
    }

    public void setUpdateSuccessHintVisibility(int updateSuccessHintVisibility) {
        this.updateSuccessHintVisibility = updateSuccessHintVisibility;
        notifyPropertyChanged(BR.updateSuccessHintVisibility);
    }

    @Bindable
    public int getPublishSuccessHintVisibility() {
        return publishSuccessHintVisibility;
    }

    public void setPublishSuccessHintVisibility(int publishSuccessHintVisibility) {
        this.publishSuccessHintVisibility = publishSuccessHintVisibility;
        notifyPropertyChanged(BR.publishSuccessHintVisibility);
    }
}
