package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 九、[需求]-需求方预支付
 * Created by zhouyifeng on 2016/10/8.
 */
public class DemandPartyPrePaymentProtocol extends BaseProtocol<String> {

    private String id;// 需求ID

    public DemandPartyPrePaymentProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_PRE_PAYMENT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public String parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
