package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十八、[需求]-服务方确认同意退款
 * Created by zhouyifeng on 2016/11/3.
 */
public class AgreeRefundProtocol extends BaseProtocol<AgreeRefundBean> {

    String id;//需求ID

    public AgreeRefundProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_AGREE_REFUND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public AgreeRefundBean parseData(String result) {
        Gson gson = new Gson();
        AgreeRefundBean agreeRefundBean = gson.fromJson(result, AgreeRefundBean.class);
        return agreeRefundBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
