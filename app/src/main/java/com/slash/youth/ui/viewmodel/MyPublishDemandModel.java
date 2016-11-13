package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.databinding.ItemDemandFlowLogBinding;
import com.slash.youth.domain.DemandFlowLogList;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyPublishDemandModel extends BaseObservable {

    ActivityMyPublishDemandBinding mActivityMyPublishDemandBinding;
    Activity mActivity;
    long tid;//需求ID
    int type;//取值范围只能是: 1需求 2服务
    int roleid;//表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者

    public MyPublishDemandModel(ActivityMyPublishDemandBinding activityMyPublishDemandBinding, Activity activity) {
        this.mActivityMyPublishDemandBinding = activityMyPublishDemandBinding;
        this.mActivity = activity;

        initData();
        initView();
    }

    ArrayList<DemandFlowLogList.LogInfo> listLogInfo = new ArrayList<DemandFlowLogList.LogInfo>();

    private void initData() {
        Bundle taskInfo = mActivity.getIntent().getExtras();
        tid = taskInfo.getLong("tid");
        type = taskInfo.getInt("type");
        roleid = taskInfo.getInt("roleid");

        getDemandFlowLogData();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    //申请退款
    public void refund(View v) {
        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);
        mActivity.startActivity(intentRefundActivity);
    }

    //打开支付界面
    public void openPaymentActivity(View v) {
        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);
        mActivity.startActivity(intentPaymentActivity);
    }

    private void getDemandFlowLogData() {
        DemandEngine.getDemandFlowLog(new BaseProtocol.IResultExecutor<DemandFlowLogList>() {
            @Override
            public void execute(DemandFlowLogList dataBean) {
                listLogInfo = dataBean.data.list;
                setDemandFlowLogItemData();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    public void setDemandFlowLogItemData() {
        for (int i = listLogInfo.size() - 1; i >= 0; i--) {
            DemandFlowLogList.LogInfo logInfo = listLogInfo.get(i);
            View itemLogInfo = inflateItemLogInfo(logInfo);
            mActivityMyPublishDemandBinding.llDemandFlowLogs.addView(itemLogInfo);
        }
    }

    public View inflateItemLogInfo(DemandFlowLogList.LogInfo logInfo) {
        ItemDemandFlowLogBinding itemDemandFlowLogBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_flow_log, null, false);
        ItemDemandLogModel itemDemandLogModel = new ItemDemandLogModel(itemDemandFlowLogBinding, mActivity, logInfo);
        itemDemandFlowLogBinding.setItemDemanLogModel(itemDemandLogModel);
        return itemDemandFlowLogBinding.getRoot();
    }

}
