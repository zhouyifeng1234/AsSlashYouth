package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/10/6.
 */
public class DemandPartySelectServicePartyProtocol extends BaseProtocol<String> {

    private String id;//需求ID
    private String uid;//服务方UID

    public DemandPartySelectServicePartyProtocol(String id, String uid) {
        this.id = id;
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_SELECT_SERVICE_PARTY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("uid", uid);
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
