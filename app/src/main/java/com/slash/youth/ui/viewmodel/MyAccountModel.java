package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityMyAccountBinding;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.MyAccountProtocol;
import com.slash.youth.ui.activity.TransactionRecordActivity;
import com.slash.youth.ui.activity.WithdrawalsActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SetDataUtils;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountModel extends BaseObservable {
    private ActivityMyAccountBinding activityMyAccountBinding;

    public MyAccountModel(ActivityMyAccountBinding activityMyAccountBinding) {
        this.activityMyAccountBinding = activityMyAccountBinding;
        initData();
    }

    private void initData() {
        MyAccountProtocol myAccountProtocol = new MyAccountProtocol();
        myAccountProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<MyAccountBean>() {
            @Override
            public void execute(MyAccountBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    MyAccountBean.DataBean data = dataBean.getData();
                    MyAccountBean.DataBean.DataBean1 data1 = data.getData();
                    //总的余额
                    int totalmoney = data1.getTotalmoney();
                    activityMyAccountBinding.totalMoney.setText(totalmoney+"元");

                    //提现金额
                    int currentmoney = data1.getCurrentmoney();
                    activityMyAccountBinding.currentMoney.setText(currentmoney+"元");

                    //冻结金额
                    int freezemoney = data1.getFreezemoney();
                    activityMyAccountBinding.freezeMoney.setText(freezemoney+"元");

                    //总的收入
                    int totalincome = data1.getTotalincome();
                    activityMyAccountBinding.totalincome.setText(totalincome+"元");

                    //总支出金额
                    int totaloutlay = data1.getTotaloutlay();
                    activityMyAccountBinding.totaloutlay.setText(totaloutlay+"元");


                }else {
                    LogKit.d("rescode ="+rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
            LogKit.d("result:"+result);
            }
        });
    }


    public void withdrawals(View view){
        Intent intentWithdrawalsActivity = new Intent(CommonUtils.getContext(), WithdrawalsActivity.class);
        intentWithdrawalsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentWithdrawalsActivity);
    }

}
