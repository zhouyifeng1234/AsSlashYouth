package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandFlowLogList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 七、[需求]-查看需求流程日志
 * Created by zhouyifeng on 2016/10/8.
 */
public class GetDemandFlowLogProtocol extends BaseProtocol<DemandFlowLogList> {

    private String id;//需求ID

    public GetDemandFlowLogProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DEMAND_FLOW_LOG;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public DemandFlowLogList parseData(String result) {
        Gson gson = new Gson();
        DemandFlowLogList demandFlowLogList = gson.fromJson(result, DemandFlowLogList.class);

        return demandFlowLogList;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
