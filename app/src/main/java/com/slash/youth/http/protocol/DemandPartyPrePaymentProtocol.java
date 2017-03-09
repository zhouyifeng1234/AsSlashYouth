package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PaymentBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 九、[需求]-需求方预支付
 * Created by zhouyifeng on 2016/10/8.
 */
public class DemandPartyPrePaymentProtocol extends BaseProtocol<PaymentBean> {

    private String id;// 需求ID
    private String amount;
    private String channel;
    private String pass;//支付密码 这个是原始密码经过一次MD5

    public DemandPartyPrePaymentProtocol(String id, String amount, String channel, String pass) {
        this.id = id;
        this.amount = amount;
        this.channel = channel;
        this.pass = pass;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_PRE_PAYMENT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("amount", amount);
        params.addBodyParameter("channel", channel);
        params.addBodyParameter("pass", pass);
    }

    PaymentBean paymentBean = null;

    @Override
    public PaymentBean parseData(String result) {
        return paymentBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        paymentBean = gson.fromJson(result, PaymentBean.class);
        if (paymentBean.rescode == 0 && paymentBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
