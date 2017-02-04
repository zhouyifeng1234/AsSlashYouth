package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyAccountBinding;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.MyAccountProtocol;
import com.slash.youth.ui.activity.MyAccountActivity;
import com.slash.youth.ui.activity.WithdrawalsActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CountUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountModel extends BaseObservable {
    private ActivityMyAccountBinding activityMyAccountBinding;
    private String unit = "元";
    private MyAccountActivity myAccountActivity;
    private String currentMoney;
    private int hintVisibility = View.GONE;

    public MyAccountModel(ActivityMyAccountBinding activityMyAccountBinding, MyAccountActivity myAccountActivity) {
        this.activityMyAccountBinding = activityMyAccountBinding;
        this.myAccountActivity = myAccountActivity;
        initData();
    }

    private void initData() {
        MyAccountProtocol myAccountProtocol = new MyAccountProtocol();
        myAccountProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<MyAccountBean>() {
            @Override
            public void execute(MyAccountBean dataBean) {
                int rescode = dataBean.getRescode();
                if (rescode == 0) {
                    MyAccountBean.DataBean data = dataBean.getData();
                    MyAccountBean.DataBean.DataBean1 data1 = data.getData();
                    //总的余额
                    double totalmoney = data1.getTotalmoney();
                    String totalMoney = CountUtils.DecimalFormat(totalmoney);
                    activityMyAccountBinding.totalMoney.setText(totalMoney + unit);

                    //提现金额
                    double currentmoney = data1.getCurrentmoney();
                    currentMoney = CountUtils.DecimalFormat(currentmoney);
                    activityMyAccountBinding.currentMoney.setText(currentMoney + unit);
                    activityMyAccountBinding.tvWithdrawals.setText(currentMoney + unit);

                    //冻结金额
                    double freezemoney = data1.getFreezemoney();
                    String freezeMoney = CountUtils.DecimalFormat(freezemoney);
                    activityMyAccountBinding.freezeMoney.setText(freezeMoney + unit);

                    //总的收入
                    double totalincome = data1.getTotalincome();
                    String totaLincome = CountUtils.DecimalFormat(totalincome);
                    activityMyAccountBinding.totalincome.setText(totaLincome + unit);

                    //总支出金额
                    double totaloutlay = data1.getTotaloutlay();
                    String totalOutLay = CountUtils.DecimalFormat(totaloutlay);
                    activityMyAccountBinding.totaloutlay.setText(totalOutLay + unit);

                } else {
                    LogKit.d("rescode =" + rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    //提现
    public void withdrawals(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_ACCOUNT_CLICK_WITHDRAW);

        Intent intentWithdrawalsActivity = new Intent(CommonUtils.getContext(), WithdrawalsActivity.class);
        intentWithdrawalsActivity.putExtra("currentMoney", currentMoney);
        intentWithdrawalsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentWithdrawalsActivity);
    }

    @Bindable
    public int getHintVisibility() {
        return hintVisibility;
    }

    public void setHintVisibility(int hintVisibility) {
        this.hintVisibility = hintVisibility;
        notifyPropertyChanged(BR.hintVisibility);
    }

    //提示
    public void hint(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_ACCOUNT_CLICK_MY_FREEZE_MONEY_RIGHT_QUESTION);

        setHintVisibility(View.VISIBLE);

      /*  DialogUtils.showDialogOne(myAccountActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {
                LogKit.d("canncel");
            }
        }, "", "");*/
    }

    //关闭提示框
    public void closeHintVisibility(View view){
        setHintVisibility(View.GONE);
    }
}
