package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPaymentBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.PaymentBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class PaymentModel extends BaseObservable {

    public static final int PAY_TYPE_BALANCE = 1;//余额支付
    public static final int PAY_TYPE_ALIPAY = 2;//支付宝支付
    public static final int PAY_TYPE_WEIXIN_PAY = 3;//微信支付

    ActivityPaymentBinding mActivityPaymentBinding;
    Activity mActivity;
    int currentCheckedPayType = PAY_TYPE_BALANCE;//当前选中的支付方式,默认选中了余额支付
    long tid;//任务ID（需求ID）
    double quote;//报价，最终以服务方的报价为准

    public PaymentModel(ActivityPaymentBinding activityPaymentBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPaymentBinding = activityPaymentBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        Bundle payInfo = mActivity.getIntent().getExtras();
        tid = payInfo.getLong("tid");
        quote = payInfo.getDouble("quote");
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    boolean isSetTradePassword;

    //去支付
    public void payment(View v) {
        if (currentCheckedPayType == PAY_TYPE_BALANCE) {
//            ToastUtils.shortToast("余额支付");
            AccountManager.getTradePasswordStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    if (dataBean.data.status == 1) {//设置了交易密码 需输入交易密码
                        isSetTradePassword = true;
                    } else if (dataBean.data.status == 0) {//表示当前没有交易密码
                        isSetTradePassword = false;
                        doBalancePayment();
                    } else {
                        LogKit.v("状态异常");
                    }
                }

                @Override
                public void executeResultError(String result) {

                }
            });

        } else if (currentCheckedPayType == PAY_TYPE_ALIPAY) {
            ToastUtils.shortToast("支付宝支付");
        } else if (currentCheckedPayType == PAY_TYPE_WEIXIN_PAY) {
            ToastUtils.shortToast("微信支付");
        }
    }


    private void doBalancePayment() {
        DemandEngine.demandPartyPrePayment(new BaseProtocol.IResultExecutor<PaymentBean>() {
            @Override
            public void execute(PaymentBean dataBean) {
                //余额支付成功
                setPaymentTopText("支付结果");
                setCloseVisibility(View.VISIBLE);
                setGobackVisibility(View.INVISIBLE);
                setPayMainInfoVisibility(View.GONE);
                setPaySuccessVisibility(View.VISIBLE);
                if (isSetTradePassword) {
                    setSetTradePasswordVisibility(View.GONE);
                } else {
                    setSetTradePasswordVisibility(View.VISIBLE);
                }
            }

            @Override
            public void executeResultError(String result) {
                //余额支付失败
                setPayFailVisibility(View.VISIBLE);
            }
        }, tid + "", quote + "", 0 + "");
    }

    public void closePaymentActivity(View v) {
        mActivity.finish();
    }

    //跳转到设置交易密码的界面（或者就在当前界面设置交易密码？）
    public void gotoSettingTransactionPassword(View v) {

    }

    //选中余额支付
    public void checkBalancePayment(View v) {
        currentCheckedPayType = PAY_TYPE_BALANCE;
        setPaymentTypeCheckedIcon(R.mipmap.pitchon_btn, R.mipmap.service_ptype_moren_icon, R.mipmap.service_ptype_moren_icon);
    }

    //选中支付宝支付
    public void checkAlipay(View v) {
        currentCheckedPayType = PAY_TYPE_ALIPAY;
        setPaymentTypeCheckedIcon(R.mipmap.service_ptype_moren_icon, R.mipmap.pitchon_btn, R.mipmap.service_ptype_moren_icon);
    }

    //选中微信支付
    public void checkWeixinPayment(View v) {
        currentCheckedPayType = PAY_TYPE_WEIXIN_PAY;
        setPaymentTypeCheckedIcon(R.mipmap.service_ptype_moren_icon, R.mipmap.service_ptype_moren_icon, R.mipmap.pitchon_btn);
    }

    private void setPaymentTypeCheckedIcon(int balanceIcon, int alipayIcon, int weixinIcon) {
        mActivityPaymentBinding.ivPaymentBalance.setImageResource(balanceIcon);
        mActivityPaymentBinding.ivPaymentAlipay.setImageResource(alipayIcon);
        mActivityPaymentBinding.ivPaymentWeixin.setImageResource(weixinIcon);
    }

    public void closeFailDialog(View v) {
        setPayFailVisibility(View.GONE);
    }

    public void closeInputPasswordBox(View v) {
        setInputTradePasswordBoxVisibility(View.GONE);
    }

    public void closeSurePasswordBox(View v) {
        setSureTradePasswordBoxVisibility(View.GONE);
    }

    public void closeSetPasswordBox(View v) {
        setSetTradePasswordBoxVisibility(View.GONE);
    }


    private int closeVisibility = View.INVISIBLE;//右上角的关闭按钮是否可见
    private int gobackVisibility = View.VISIBLE;//左上角的回退箭头是否可见
    private String paymentTopText = "预支付";//支付页面标题栏显示的文本
    private int paySuccessVisibility = View.GONE;//支付成功后的显示页面
    private int setTradePasswordVisibility;//支付完成后的页面，如果还没有设置交易密码，就会显示
    private int payMainInfoVisibility = View.VISIBLE;//支付主界面是否显示，一开始需要显示
    private int payFailVisibility = View.GONE;//支付失败页面是否显示
    private int inputPasswordVisibility = View.GONE;//输入交易密码的页面是否显示
    private int setTradePasswordBoxVisibility = View.GONE;//设置交易密码的密码框
    private int sureTradePasswordBoxVisibility = View.GONE;//设置之后确认交易密码的密码框
    private int inputTradePasswordBoxVisibility = View.GONE;//选择余额支付时，输入交易密码的密码框

    @Bindable
    public int getCloseVisibility() {
        return closeVisibility;
    }

    public void setCloseVisibility(int closeVisibility) {
        this.closeVisibility = closeVisibility;
        notifyPropertyChanged(BR.closeVisibility);
    }

    @Bindable
    public int getGobackVisibility() {
        return gobackVisibility;
    }

    public void setGobackVisibility(int gobackVisibility) {
        this.gobackVisibility = gobackVisibility;
        notifyPropertyChanged(BR.gobackVisibility);
    }

    @Bindable
    public String getPaymentTopText() {
        return paymentTopText;
    }

    public void setPaymentTopText(String paymentTopText) {
        this.paymentTopText = paymentTopText;
        notifyPropertyChanged(BR.paymentTopText);
    }

    @Bindable
    public int getPaySuccessVisibility() {
        return paySuccessVisibility;
    }

    public void setPaySuccessVisibility(int paySuccessVisibility) {
        this.paySuccessVisibility = paySuccessVisibility;
        notifyPropertyChanged(BR.paySuccessVisibility);
    }

    @Bindable
    public int getSetTradePasswordVisibility() {
        return setTradePasswordVisibility;
    }

    public void setSetTradePasswordVisibility(int setTradePasswordVisibility) {
        this.setTradePasswordVisibility = setTradePasswordVisibility;
        notifyPropertyChanged(BR.setTradePasswordVisibility);
    }

    @Bindable
    public int getPayMainInfoVisibility() {
        return payMainInfoVisibility;
    }

    public void setPayMainInfoVisibility(int payMainInfoVisibility) {
        this.payMainInfoVisibility = payMainInfoVisibility;
        notifyPropertyChanged(BR.payMainInfoVisibility);
    }

    @Bindable
    public int getPayFailVisibility() {
        return payFailVisibility;
    }

    public void setPayFailVisibility(int payFailVisibility) {
        this.payFailVisibility = payFailVisibility;
        notifyPropertyChanged(BR.payFailVisibility);
    }

    @Bindable
    public int getInputPasswordVisibility() {
        return inputPasswordVisibility;
    }

    public void setInputPasswordVisibility(int inputPasswordVisibility) {
        this.inputPasswordVisibility = inputPasswordVisibility;
        notifyPropertyChanged(BR.inputPasswordVisibility);
    }

    @Bindable
    public int getSetTradePasswordBoxVisibility() {
        return setTradePasswordBoxVisibility;
    }

    public void setSetTradePasswordBoxVisibility(int setTradePasswordBoxVisibility) {
        this.setTradePasswordBoxVisibility = setTradePasswordBoxVisibility;
        notifyPropertyChanged(BR.setTradePasswordBoxVisibility);
    }

    @Bindable
    public int getSureTradePasswordBoxVisibility() {
        return sureTradePasswordBoxVisibility;
    }

    public void setSureTradePasswordBoxVisibility(int sureTradePasswordBoxVisibility) {
        this.sureTradePasswordBoxVisibility = sureTradePasswordBoxVisibility;
        notifyPropertyChanged(BR.sureTradePasswordBoxVisibility);
    }

    @Bindable
    public int getInputTradePasswordBoxVisibility() {
        return inputTradePasswordBoxVisibility;
    }

    public void setInputTradePasswordBoxVisibility(int inputTradePasswordBoxVisibility) {
        this.inputTradePasswordBoxVisibility = inputTradePasswordBoxVisibility;
        notifyPropertyChanged(BR.inputTradePasswordBoxVisibility);
    }
}
