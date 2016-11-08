package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyAccountBinding;
import com.slash.youth.ui.activity.TransactionRecordActivity;
import com.slash.youth.ui.activity.WithdrawalsActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountModel extends BaseObservable {
    private ActivityMyAccountBinding activityMyAccountBinding;

    public MyAccountModel(ActivityMyAccountBinding activityMyAccountBinding) {
        this.activityMyAccountBinding = activityMyAccountBinding;
    }

    public void withdrawals(View view){
        LogKit.d("提现");
        Intent intentWithdrawalsActivity = new Intent(CommonUtils.getContext(), WithdrawalsActivity.class);
        intentWithdrawalsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentWithdrawalsActivity);
    }


}
