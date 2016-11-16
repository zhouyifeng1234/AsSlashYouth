package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityRefundBinding;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class RefundModel extends BaseObservable {

    ActivityRefundBinding mActivityRefundBinding;
    Activity mActivity;
    String currentCheckedReason = "";//选择的退款理由，默认都不选

    public RefundModel(ActivityRefundBinding activityRefundBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityRefundBinding = activityRefundBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void refund(View v) {
        String otherRefundReason = mActivityRefundBinding.etRefundOtherReason.getText().toString();

    }

    public void checkReason(View v) {
        switch (v.getId()) {
            case R.id.ll_refund_reason_late_to_address:
                currentCheckedReason = mActivityRefundBinding.tvRefundReasonLateToAddress.getText().toString();
                setRefundReasonCheckedIcon(R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_ability_low:
                currentCheckedReason = mActivityRefundBinding.tvRefundReasonAbilityLow.getText().toString();
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_attitude_bad:
                currentCheckedReason = mActivityRefundBinding.tvRefundReasonAttitudeBad.getText().toString();
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_canot_service:
                currentCheckedReason = mActivityRefundBinding.tvRefundReasonCanotService.getText().toString();
                setRefundReasonCheckedIcon(R.mipmap.moren, R.mipmap.moren, R.mipmap.moren, R.mipmap.xuanzhogn, R.mipmap.moren);
                break;
            case R.id.ll_refund_reason_demand_change:
                currentCheckedReason = mActivityRefundBinding.tvRefundReasonDemandChange.getText().toString();
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
