package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/26.
 */
public class DemandRefundProtocol extends BaseProtocol<CommonResultBean> {
    private String id;//需求ID
    private String reason;//按照目前的客户端逻辑，reason和reasondetail至少传其中一个字段
    private String reasondetail;//按照目前的客户端逻辑，reason和reasondetail至少传其中一个字段

    public DemandRefundProtocol(String id, String reason, String reasondetail) {
        this.id = id;
        this.reason = reason;
        this.reasondetail = reasondetail;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_REFUND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("reason", reason);
        params.addBodyParameter("reasondetail", reasondetail);
    }

    @Override
    public CommonResultBean parseData(String result) {
        return commonResultBean;
    }

    CommonResultBean commonResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonResultBean = gson.fromJson(result, CommonResultBean.class);
        if (commonResultBean.rescode == 0 && commonResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
