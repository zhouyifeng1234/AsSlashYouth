package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 二、[服务]-查看服务详情
 * <p/>
 * Created by zhouyifeng on 2016/11/30.
 */
public class ServiceDetailProtocol extends BaseProtocol<ServiceDetailBean> {

    String id;

    public ServiceDetailProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_DETAIL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public ServiceDetailBean parseData(String result) {
        return serviceDetailBean;
    }

    ServiceDetailBean serviceDetailBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceDetailBean = gson.fromJson(result, ServiceDetailBean.class);
        if (serviceDetailBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
