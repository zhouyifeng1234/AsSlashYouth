package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PaymentBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;

import org.xutils.http.RequestParams;

/**
 * 六、[服务]-需求方预支付
 * <p/>
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceFlowPaymentProtocol extends BaseProtocol<PaymentBean> {

    private String soid;
    private String amount;
    private String channel;
    private String pass;//支付密码 这个是原始密码经过一次MD5

    public ServiceFlowPaymentProtocol(String soid, String amount, String channel, String pass) {
        this.soid = soid;
        this.amount = amount;
        this.channel = channel;
        this.pass = pass;
    }


    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_FLOW_PAYMENT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
        params.addBodyParameter("amount", amount);
        params.addBodyParameter("channel", channel);
        params.addBodyParameter("pass", pass);
    }

    @Override
    public PaymentBean parseData(String result) {
        return paymentBean;
    }

    PaymentBean paymentBean;

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
