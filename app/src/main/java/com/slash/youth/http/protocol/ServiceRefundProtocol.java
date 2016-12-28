package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/26.
 */
public class ServiceRefundProtocol extends BaseProtocol<CommonResultBean> {

    private String soid;// 服务订单ID
    private String reason;//  1、2、3   reason和reasondetail至少有一个
    private String reasondetail;//退款原因详细   reason和reasondetail至少有一个

    public ServiceRefundProtocol(String soid, String reason, String reasondetail) {
        this.soid = soid;
        this.reason = reason;
        this.reasondetail = reasondetail;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_REFUND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
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
