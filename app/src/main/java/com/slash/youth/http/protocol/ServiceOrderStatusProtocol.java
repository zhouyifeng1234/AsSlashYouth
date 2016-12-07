package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceOrderStatusBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class ServiceOrderStatusProtocol extends BaseProtocol<ServiceOrderStatusBean> {

    private String soid;

    public ServiceOrderStatusProtocol(String soid) {
        this.soid = soid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_SERVICE_ORDER_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
    }

    @Override
    public ServiceOrderStatusBean parseData(String result) {
        return serviceOrderStatusBean;
    }

    ServiceOrderStatusBean serviceOrderStatusBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceOrderStatusBean = gson.fromJson(result, ServiceOrderStatusBean.class);
        if (serviceOrderStatusBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
