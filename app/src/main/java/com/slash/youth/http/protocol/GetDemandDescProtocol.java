package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十四、[需求]-加载需求描述信息
 * Created by zhouyifeng on 2016/10/8.
 */
public class GetDemandDescProtocol extends BaseProtocol<String> {
    private String id;//需求ID

    public GetDemandDescProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DEMAND_DESC;
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
