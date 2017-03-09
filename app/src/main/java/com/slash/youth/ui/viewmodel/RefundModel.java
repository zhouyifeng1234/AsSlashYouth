package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityRefundBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class RefundModel extends BaseObservable {

    ActivityRefundBinding mActivityRefundBinding;
    Activity mActivity;
    String currentCheckedReason = "";//选择的退款理由，默认都不选

    private long tid;//需求ID或者服务订单ID
    private int type = -1;//1需求 2服务

    public RefundModel(ActivityRefundBinding activityRefundBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityRefundBinding = activityRefundBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        tid = mActivity.getIntent().getLongExtra("tid", -1);
        type = mActivity.getIntent().getIntExtra("type", -1);
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void refund(View v) {
        String otherRefundReason = mActivityRefundBinding.etRefundOtherReason.getText().toString();
        if (TextUtils.isEmpty(otherRefundReason) && TextUtils.isEmpty(currentCheckedReason)) {
            ToastUtils.shortToast("至少选择或填写一个退款理由");
            return;
        }
        if (type == 1) {
            //需求
            DemandEngine.refund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("申请退款成功");
                    CommonUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.finish();
                        }
                    }, 2000);
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("申请退款失败:" + result);
                }
            }, tid + "", currentCheckedReason, otherRefundReason);
        } else {
            //服务
            ServiceEngine.refund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("申请退款成功");
                    CommonUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.finish();
                        }
                    }, 2000);
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("申请退款失败:" + result);
                }
            }, tid + "", currentCheckedReason, otherRefundReason);
        }
    }

    public void checkReason(View v) {
        switch (v.getId()) {
            case R.id.ll_refund_reason_late_to_address:
//                currentCheckedReason = mActivityRefundBinding.tvRefundReasonLateToAddress.getText().toString();
                currentCheckedReason = "1";
                setRefundReasonCheckedIcon(R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_ability_low:
//                currentCheckedReason = mActivityRefundBinding.tvRefundReasonAbilityLow.getText().toString();
                currentCheckedReason = "2";
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_attitude_bad:
//                currentCheckedReason = mActivityRefundBinding.tvRefundReasonAttitudeBad.getText().toString();
                currentCheckedReason = "3";
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_canot_service:
//                currentCheckedReason = mActivityRefundBinding.tvRefundReasonCanotService.getText().toString();
                currentCheckedReason = "4";
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_demand_change:
//                currentCheckedReason = mActivityRefundBinding.tvRefundReasonDemandChange.getText().toString();
                currentCheckedReason = "5";
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.xuanzhogn);
                break;
        }
    }

    private void setRefundReasonCheckedIcon(int lateToAddressCheckedIcon, int abilityLowCheckedIcon, int attitudeBadCheckedIcon, int cannotServiceCheckedIcon, int demandChangeCheckedIcon) {
        mActivityRefundBinding.ivRefundReasonLateToAddress.setImageResource(lateToAddressCheckedIcon);
        mActivityRefundBinding.ivRefundReasonAbilityLow.setImageResource(abilityLowCheckedIcon);
        mActivityRefundBinding.ivRefundReasonAttitudeBad.setImageResource(attitudeBadCheckedIcon);
        mActivityRefundBinding.ivRefundReasonCanotService.setImageResource(cannotServiceCheckedIcon);
        mActivityRefundBinding.ivRefundReasonDemandChange.setImageResource(demandChangeCheckedIcon);
    }
}
