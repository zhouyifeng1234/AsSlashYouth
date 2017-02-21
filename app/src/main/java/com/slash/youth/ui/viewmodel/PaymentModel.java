package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPaymentBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.domain.PaymentBean;
import com.slash.youth.domain.ThirdPayChargeBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.MD5Utils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouyifeng on 2016/11/12.
 */
public class PaymentModel extends BaseObservable {

    public static final int PAY_TYPE_BALANCE = 1;//余额支付
    public static final int PAY_TYPE_ALIPAY = 2;//支付宝支付
    public static final int PAY_TYPE_WEIXIN_PAY = 3;//微信支付

    public static final int INPUT_PASSWORD_TYPE_INPUT = 1001;//输入交易密码
    public static final int INPUT_PASSWORD_TYPE_SETTING = 1002;//设置交易密码
    public static final int INPUT_PASSWORD_TYPE_SURE = 1003;//确认交易密码

    ActivityPaymentBinding mActivityPaymentBinding;
    Activity mActivity;
    int currentCheckedPayType = PAY_TYPE_BALANCE;//当前选中的支付方式,默认选中了余额支付
    int inputPasswordType = INPUT_PASSWORD_TYPE_INPUT;
    long tid;//任务ID（需求ID）
    double quote;//报价，最终以服务方的报价为准
    String title;//服务或者需求标题

    int type = -1;//1需求 2服务
    double balance;

    public PaymentModel(ActivityPaymentBinding activityPaymentBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPaymentBinding = activityPaymentBinding;
        displayLoadLayer();
        initData();
        initView();
    }

    private void initData() {
        Bundle payInfo = mActivity.getIntent().getExtras();
        tid = payInfo.getLong("tid");
        quote = payInfo.getDouble("quote");
        type = payInfo.getInt("type");
        title = payInfo.getString("title");

        setPayMoney("￥" + (int) quote);
        setTaskTitle(title);
        AccountManager.getMyAccountInfo(new BaseProtocol.IResultExecutor<MyAccountBean>() {
            @Override
            public void execute(MyAccountBean dataBean) {
//                balance = (int) (dataBean.getData().getData().getCurrentmoney());
                balance = dataBean.getData().getData().getCurrentmoney();
                setCurrentmoney(balance + "");
                hideLoadLayer();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取我的账户信息失败:" + result);
            }
        });

        AccountManager.getTradePasswordStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 1) {//设置了交易密码
                    isSetTradePassword = true;
                } else if (dataBean.data.status == 0) {//表示当前没有交易密码
                    isSetTradePassword = false;
                } else {
                    LogKit.v("状态异常");
                }
            }

            @Override
            public void executeResultError(String result) {
                //这里不会执行
            }
        });
    }

    private void initView() {

    }

    /**
     * 刚进入页面时，显示加载层
     */
    private void displayLoadLayer() {
        setLoadLayerVisibility(View.VISIBLE);
    }

    /**
     * 数据加载完毕后,隐藏加载层
     */
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    boolean isSetTradePassword;

    //去支付
    public void payment(View v) {
        if (currentCheckedPayType == PAY_TYPE_BALANCE) {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY_BALANCE);
//            ToastUtils.shortToast("余额支付");

            //余额支付,这里不需要判断有没有交易密码，因为按照正常的逻辑走下来，这里肯定会有交易密码
            //首先输入交易密码
            if (quote > balance) {
                ToastUtils.shortToast("余额不足");
            } else {
                inputPasswordType = INPUT_PASSWORD_TYPE_INPUT;
                setInputPasswordHintText("请输入交易密码");
                setInputPasswordVisibility(View.VISIBLE);
            }
        } else if (currentCheckedPayType == PAY_TYPE_ALIPAY) {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY_ALIPAY);
//            ToastUtils.shortToast("支付宝支付");
            doAlipay();
        } else if (currentCheckedPayType == PAY_TYPE_WEIXIN_PAY) {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY_WECHAT);
//            ToastUtils.shortToast("微信支付");
            doWXpayment();
        }

    }


    /**
     * 余额支付
     */
    private void doBalancePayment(String tradePasswordStr) {
        String tradePassMd5 = MD5Utils.md5(tradePasswordStr);
        LogKit.v("tradePassMd5：" + tradePassMd5);
        if (type == 1) {//任务类型是需求 中的支付
            LogKit.v("tid:" + tid + "  quote:" + quote);
            DemandEngine.demandPartyPrePayment(new BaseProtocol.IResultExecutor<PaymentBean>() {
                @Override
                public void execute(PaymentBean dataBean) {
                    //余额支付成功
                    doPaySuccess();
                }

                @Override
                public void executeResultError(String result) {
                    //余额支付失败
                    Gson gson = new Gson();
                    PaymentBean paymentBean = gson.fromJson(result, PaymentBean.class);
                    int status = paymentBean.data.status;
                    if (status == 0) {
                        setPayFailReason("服务端底层错误");
                    } else if (status == 2) {
                        setPayFailReason("支付状态错误");
                    } else if (status == 3) {
                        setPayFailReason("订单错误");
                    } else if (status == 4) {
                        setPayFailReason("重复支付错误");
                    } else if (status == 5) {
                        setPayFailReason("支付金额错误");
                    } else if (status == 6) {
                        setPayFailReason("支付密码错误");
                    }
                    setPayFailVisibility(View.VISIBLE);
                }
            }, tid + "", quote + "", 0 + "", tradePassMd5);
        } else if (type == 2) {//任务类型是服务 中的支付
            LogKit.v("tid:" + tid + "  quote:" + quote);
            ServiceEngine.servicePayment(new BaseProtocol.IResultExecutor<PaymentBean>() {
                @Override
                public void execute(PaymentBean dataBean) {
                    //余额支付成功
                    doPaySuccess();
                }

                @Override
                public void executeResultError(String result) {
                    //余额支付失败
                    Gson gson = new Gson();
                    PaymentBean paymentBean = gson.fromJson(result, PaymentBean.class);
                    int status = paymentBean.data.status;
                    if (status == 0) {
                        setPayFailReason("服务端底层错误");
                    } else if (status == 2) {
                        setPayFailReason("支付状态错误");
                    } else if (status == 3) {
                        setPayFailReason("订单错误");
                    } else if (status == 4) {
                        setPayFailReason("重复支付错误");
                    } else if (status == 5) {
                        setPayFailReason("支付金额错误");
                    } else if (status == 6) {
                        setPayFailReason("支付密码错误");
                    }
                    setPayFailVisibility(View.VISIBLE);
                }
            }, tid + "", quote + "", 0 + "", tradePassMd5);
        }
        //隐藏密码输入框，并清空输入的密码
        closeInputPasswordBox(null);
    }

    /**
     * 支付宝支付
     */
    private void doAlipay() {
        if (type == 1) {//任务类型是需求 中的支付
            DemandEngine.demandThirdPay(new BaseProtocol.IResultExecutor<ThirdPayChargeBean>() {
                @Override
                public void execute(ThirdPayChargeBean dataBean) {
                    Gson gson = new Gson();
                    String chargeJson = gson.toJson(dataBean.data.charge);
                    LogKit.v("chargeJson:" + chargeJson);
                    Pingpp.createPayment(mActivity, chargeJson);

                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取charge对象失败");
                }
            }, tid + "", quote + "", "1");
        } else if (type == 2) {//任务类型是服务 中的支付
            ServiceEngine.serviceThirdPay(new BaseProtocol.IResultExecutor<ThirdPayChargeBean>() {
                @Override
                public void execute(ThirdPayChargeBean dataBean) {
                    Gson gson = new Gson();
                    String chargeJson = gson.toJson(dataBean.data.charge);
                    LogKit.v("chargeJson:" + chargeJson);
                    Pingpp.createPayment(mActivity, chargeJson);

                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取charge对象失败");
                }
            }, tid + "", quote + "", "1");
        }
    }

    /**
     * 微信支付
     */
    private void doWXpayment() {
        if (type == 1) {//任务类型是需求 中的支付
            DemandEngine.demandThirdPay(new BaseProtocol.IResultExecutor<ThirdPayChargeBean>() {
                @Override
                public void execute(ThirdPayChargeBean dataBean) {
                    Gson gson = new Gson();
                    String chargeJson = gson.toJson(dataBean.data.charge);
                    LogKit.v("chargeJson:" + chargeJson);
                    Pingpp.createPayment(mActivity, chargeJson);

                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取charge对象失败");
                }
            }, tid + "", quote + "", "2");
        } else if (type == 2) {//任务类型是服务 中的支付
            ServiceEngine.serviceThirdPay(new BaseProtocol.IResultExecutor<ThirdPayChargeBean>() {
                @Override
                public void execute(ThirdPayChargeBean dataBean) {
                    Gson gson = new Gson();
                    String chargeJson = gson.toJson(dataBean.data.charge);
                    LogKit.v("chargeJson:" + chargeJson);
                    Pingpp.createPayment(mActivity, chargeJson);

                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取charge对象失败");
                }
            }, tid + "", quote + "", "2");
        }
    }

    /**
     * 支付成功之后执行
     */
    public void doPaySuccess() {
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


    /**
     * 第三方支付失败后执行
     */
    public void doThirdPayFail() {
        setPayFailReason("网络连接失败，请重新支付");
        setPayFailVisibility(View.VISIBLE);
    }


    public void closePaymentActivity(View v) {
        mActivity.finish();
    }

    //跳转到设置交易密码的界面（或者就在当前界面设置交易密码？）
    public void gotoSettingTransactionPassword(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY_SET_TRADE_PASSWORD);

        inputPasswordType = INPUT_PASSWORD_TYPE_SETTING;
        setInputPasswordHintText("设置交易密码");
        setInputPasswordVisibility(View.VISIBLE);
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
        //隐藏密码输入框，并清空输入的密码
        setInputPasswordVisibility(View.GONE);
        inputPasswordCount = 0;
        setPassChar1Visibility(View.INVISIBLE);
        setPassChar2Visibility(View.INVISIBLE);
        setPassChar3Visibility(View.INVISIBLE);
        setPassChar4Visibility(View.INVISIBLE);
        setPassChar5Visibility(View.INVISIBLE);
        setPassChar6Visibility(View.INVISIBLE);
    }

    private int inputPasswordCount = 0;
    private char[] passwordCharArr = new char[6];

    private String settingPassword = "";
    private String surePassword = "";

    public void inputPassword(View v) {
        switch (v.getId()) {
            case R.id.fl_delete_trade_password:
                //删除密码按钮
                if (inputPasswordCount > 0) {
                    if (inputPasswordCount == 1) {
                        setPassChar1Visibility(View.INVISIBLE);
                    } else if (inputPasswordCount == 2) {
                        setPassChar2Visibility(View.INVISIBLE);
                    } else if (inputPasswordCount == 3) {
                        setPassChar3Visibility(View.INVISIBLE);
                    } else if (inputPasswordCount == 4) {
                        setPassChar4Visibility(View.INVISIBLE);
                    } else if (inputPasswordCount == 5) {
                        setPassChar5Visibility(View.INVISIBLE);
                    } else {
                        setPassChar6Visibility(View.INVISIBLE);
                    }
                    inputPasswordCount--;
                }
                break;
            default:
                //输入密码 0-9 按钮
                String passChar = ((TextView) v).getText().toString();
                if (inputPasswordCount < 6) {
                    inputPasswordCount++;
                    int addIndex = inputPasswordCount - 1;
                    passwordCharArr[addIndex] = passChar.charAt(0);
                    if (inputPasswordCount == 1) {
                        setPassChar1Visibility(View.VISIBLE);
                    } else if (inputPasswordCount == 2) {
                        setPassChar2Visibility(View.VISIBLE);
                    } else if (inputPasswordCount == 3) {
                        setPassChar3Visibility(View.VISIBLE);
                    } else if (inputPasswordCount == 4) {
                        setPassChar4Visibility(View.VISIBLE);
                    } else if (inputPasswordCount == 5) {
                        setPassChar5Visibility(View.VISIBLE);
                    } else {
                        setPassChar6Visibility(View.VISIBLE);
                    }
                    if (inputPasswordCount >= 6) {
                        String tradePasswordStr = new String(passwordCharArr);
                        //ToastUtils.shortToast(tradePasswordStr);
                        if (inputPasswordType == INPUT_PASSWORD_TYPE_INPUT) {
                            //密码输入完毕，去余额支付
                            doBalancePayment(tradePasswordStr);
                        } else if (inputPasswordType == INPUT_PASSWORD_TYPE_SETTING) {
                            //设置交易密码，第一次输入完毕
                            settingPassword = tradePasswordStr;
                            inputPasswordType = INPUT_PASSWORD_TYPE_SURE;
                            inputPasswordCount = 0;
                            setPassChar1Visibility(View.INVISIBLE);
                            setPassChar2Visibility(View.INVISIBLE);
                            setPassChar3Visibility(View.INVISIBLE);
                            setPassChar4Visibility(View.INVISIBLE);
                            setPassChar5Visibility(View.INVISIBLE);
                            setPassChar6Visibility(View.INVISIBLE);
                            setInputPasswordHintText("确认交易密码");
                        } else if (inputPasswordType == INPUT_PASSWORD_TYPE_SURE) {
                            //确认交易密码
                            surePassword = tradePasswordStr;
                            if (settingPassword.equals(surePassword)) {
                                //创建交易密码
                                String surePasswordMd5 = MD5Utils.md5(surePassword);
                                AccountManager.createTradePassword(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                                    @Override
                                    public void execute(CommonResultBean dataBean) {
                                        //创建成功，需要隐藏设置密码按钮
                                        isSetTradePassword = true;
                                        setSetTradePasswordVisibility(View.GONE);
                                    }

                                    @Override
                                    public void executeResultError(String result) {
                                        ToastUtils.shortToast("创建交易密码失败:" + result);
                                    }
                                }, surePasswordMd5);

                                closeInputPasswordBox(null);
                            } else {
                                ToastUtils.shortToast("两次密码不一致，请重新输入");
                                surePassword = "";
                                settingPassword = "";
                                inputPasswordType = INPUT_PASSWORD_TYPE_SETTING;
                                inputPasswordCount = 0;
                                setPassChar1Visibility(View.INVISIBLE);
                                setPassChar2Visibility(View.INVISIBLE);
                                setPassChar3Visibility(View.INVISIBLE);
                                setPassChar4Visibility(View.INVISIBLE);
                                setPassChar5Visibility(View.INVISIBLE);
                                setPassChar6Visibility(View.INVISIBLE);
                                setInputPasswordHintText("输入交易密码");
                            }
                        }
                    }
                }
                break;
        }
    }

    private int closeVisibility = View.INVISIBLE;//右上角的关闭按钮是否可见
    private int gobackVisibility = View.VISIBLE;//左上角的回退箭头是否可见
    private String paymentTopText = "预支付";//支付页面标题栏显示的文本
    private int paySuccessVisibility = View.GONE;//支付成功后的显示页面
    private int setTradePasswordVisibility;//支付完成后的页面，如果还没有设置交易密码，就会显示
    private int payMainInfoVisibility = View.VISIBLE;//支付主界面是否显示，一开始需要显示
    private int payFailVisibility = View.GONE;//支付失败页面是否显示
    private int inputPasswordVisibility = View.GONE;//输入交易密码的页面是否显示

    private int passChar1Visibility = View.INVISIBLE;
    private int passChar2Visibility = View.INVISIBLE;
    private int passChar3Visibility = View.INVISIBLE;
    private int passChar4Visibility = View.INVISIBLE;
    private int passChar5Visibility = View.INVISIBLE;
    private int passChar6Visibility = View.INVISIBLE;

    private int loadLayerVisibility;
    private String currentmoney;
    private String payMoney;
    private String taskTitle;
    private String payFailReason;
    private String inputPasswordHintText;

    @Bindable
    public String getInputPasswordHintText() {
        return inputPasswordHintText;
    }

    public void setInputPasswordHintText(String inputPasswordHintText) {
        this.inputPasswordHintText = inputPasswordHintText;
        notifyPropertyChanged(BR.inputPasswordHintText);
    }

    @Bindable
    public String getPayFailReason() {
        return payFailReason;
    }

    public void setPayFailReason(String payFailReason) {
        this.payFailReason = payFailReason;
        notifyPropertyChanged(BR.payFailReason);
    }

    @Bindable
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        notifyPropertyChanged(BR.taskTitle);
    }

    @Bindable
    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
        notifyPropertyChanged(BR.payMoney);
    }

    @Bindable
    public String getCurrentmoney() {
        return currentmoney;
    }

    public void setCurrentmoney(String currentmoney) {
        this.currentmoney = currentmoney;
        notifyPropertyChanged(BR.currentmoney);
    }

    @Bindable
    public int getLoadLayerVisibility() {
        return loadLayerVisibility;
    }

    public void setLoadLayerVisibility(int loadLayerVisibility) {
        this.loadLayerVisibility = loadLayerVisibility;
        notifyPropertyChanged(BR.loadLayerVisibility);
    }

    @Bindable
    public int getPassChar1Visibility() {
        return passChar1Visibility;
    }

    public void setPassChar1Visibility(int passChar1Visibility) {
        this.passChar1Visibility = passChar1Visibility;
        notifyPropertyChanged(BR.passChar1Visibility);
    }

    @Bindable
    public int getPassChar2Visibility() {
        return passChar2Visibility;
    }

    public void setPassChar2Visibility(int passChar2Visibility) {
        this.passChar2Visibility = passChar2Visibility;
        notifyPropertyChanged(BR.passChar2Visibility);
    }

    @Bindable
    public int getPassChar3Visibility() {
        return passChar3Visibility;
    }

    public void setPassChar3Visibility(int passChar3Visibility) {
        this.passChar3Visibility = passChar3Visibility;
        notifyPropertyChanged(BR.passChar3Visibility);
    }

    @Bindable
    public int getPassChar4Visibility() {
        return passChar4Visibility;
    }

    public void setPassChar4Visibility(int passChar4Visibility) {
        this.passChar4Visibility = passChar4Visibility;
        notifyPropertyChanged(BR.passChar4Visibility);
    }

    @Bindable
    public int getPassChar5Visibility() {
        return passChar5Visibility;
    }

    public void setPassChar5Visibility(int passChar5Visibility) {
        this.passChar5Visibility = passChar5Visibility;
        notifyPropertyChanged(BR.passChar5Visibility);
    }

    @Bindable
    public int getPassChar6Visibility() {
        return passChar6Visibility;
    }

    public void setPassChar6Visibility(int passChar6Visibility) {
        this.passChar6Visibility = passChar6Visibility;
        notifyPropertyChanged(BR.passChar6Visibility);
    }

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
}
