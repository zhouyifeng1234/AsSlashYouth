package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十三、[需求]-需求方查看竞标（抢需求服务者）列表
 * Created by zhouyifeng on 2016/10/8.
 */
public class DemandPartyGetBidListProtocol extends BaseProtocol<String> {
    private String id;// 需求ID

    public DemandPartyGetBidListProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_GET_BIDLIST;
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
