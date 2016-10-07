package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/10/6.
 */
public class ServicePartyBidDemandProtocol extends BaseProtocol<String> {

    private String id;
    private String quote;

    public ServicePartyBidDemandProtocol(String id, String quote) {
        this.id = id;
        this.quote = quote;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_BID_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addHeader("uid", "10001");

        params.addBodyParameter("id", id);
        params.addBodyParameter("quote", quote);
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
