package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.InterventionBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十九、[需求]-服务方不同意退款并申请平台介入
 * Created by zhouyifeng on 2016/11/3.
 */
public class InterventionProtocol extends BaseProtocol<InterventionBean> {

    String id;//需求ID

    public InterventionProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_INTERVENTION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public InterventionBean parseData(String result) {
        Gson gson = new Gson();
        InterventionBean interventionBean = gson.fromJson(result, InterventionBean.class);
        return interventionBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
