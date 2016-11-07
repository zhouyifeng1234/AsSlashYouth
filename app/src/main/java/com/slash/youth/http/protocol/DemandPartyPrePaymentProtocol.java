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

    public DemandPartyPrePaymentProtocol(String id, String amount, String channel) {
        this.id = id;
        this.amount = amount;
        this.channel = channel;
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
    }

    @Override
    public PaymentBean parseData(String result) {
        Gson gson = new Gson();
        PaymentBean paymentBean = gson.fromJson(result, PaymentBean.class);
        return paymentBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
