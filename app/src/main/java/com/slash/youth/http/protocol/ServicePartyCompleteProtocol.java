package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十一、[需求]-服务方完成任务
 * Created by zhouyifeng on 2016/10/8.
 */
public class ServicePartyCompleteProtocol extends BaseProtocol<String> {
    private String id;// 需求ID

    public ServicePartyCompleteProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_COMPLETE;
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
