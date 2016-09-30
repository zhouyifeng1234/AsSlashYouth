package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/30.
 */
public class CancelDemandProtocol extends BaseProtocol<String> {
    private String cancelDemandId;

    public CancelDemandProtocol(String cancelDemandId) {
        this.cancelDemandId = cancelDemandId;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CANCEL_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", cancelDemandId);
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
