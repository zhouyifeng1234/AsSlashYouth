package com.slash.youth.http.protocol;


import com.google.gson.Gson;
import com.slash.youth.domain.ServiceFlowLogList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/29.
 */
public class GetServiceFlowLogProtocol extends BaseProtocol<ServiceFlowLogList> {

    private String soid;

    public GetServiceFlowLogProtocol(String soid) {
        this.soid = soid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_SERVICE_FLOW_LOG;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
    }

    @Override
    public ServiceFlowLogList parseData(String result) {
        return serviceFlowLogList;
    }

    ServiceFlowLogList serviceFlowLogList;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceFlowLogList = gson.fromJson(result, ServiceFlowLogList.class);
        if (serviceFlowLogList.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
