package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/30.
 */
public class MyPublishDemandListProtocol extends BaseProtocol<String> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_PUBLISH_DEMAND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {

        params.addBodyParameter("offset", 0 + "");
        params.addBodyParameter("limit", 10 + "");

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
