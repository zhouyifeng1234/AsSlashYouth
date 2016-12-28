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
import com.slash.youth.utils.CountUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SetDataUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountModel extends BaseObservable {
    private ActivityMyAccountBinding activityMyAccountBinding;
    private String unit = "元";

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
                    double totalmoney = data1.getTotalmoney();
                    String totalMoney = CountUtils.DecimalFormat(totalmoney);
                    activityMyAccountBinding.totalMoney.setText(totalMoney+unit);

                    //提现金额
                    double currentmoney = data1.getCurrentmoney();
                    String currentMoney = CountUtils.DecimalFormat(currentmoney);
                    activityMyAccountBinding.currentMoney.setText(currentMoney+unit);
                    activityMyAccountBinding.tvWithdrawals.setText(currentMoney+unit);

                    //冻结金额
                    double freezemoney = data1.getFreezemoney();
                    String freezeMoney= CountUtils.DecimalFormat(freezemoney);
                    activityMyAccountBinding.freezeMoney.setText(freezeMoney+unit);

                    //总的收入
                    double totalincome = data1.getTotalincome();
                    String totaLincome= CountUtils.DecimalFormat(totalincome);
                    activityMyAccountBinding.totalincome.setText(totaLincome+unit);

                    //总支出金额
                    double totaloutlay = data1.getTotaloutlay();
                    String totalOutLay= CountUtils.DecimalFormat(totaloutlay);
                    activityMyAccountBinding.totaloutlay.setText(totalOutLay+unit);

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

    //提现
    public void withdrawals(View view){
        Intent intentWithdrawalsActivity = new Intent(CommonUtils.getContext(), WithdrawalsActivity.class);
        intentWithdrawalsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentWithdrawalsActivity);
    }

    //提示
    public void hint(View view){
        ToastUtils.shortCenterToast("提示框");
    }


}
