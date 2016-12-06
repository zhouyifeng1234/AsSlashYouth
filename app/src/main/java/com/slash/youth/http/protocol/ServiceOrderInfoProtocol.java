package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 获取服务订单信息，有status
 * <p/>
 * Created by zhouyifeng on 2016/12/6.
 */
public class ServiceOrderInfoProtocol extends BaseProtocol<ServiceOrderInfoBean> {

    private String soid;

    public ServiceOrderInfoProtocol(String soid) {
        this.soid = soid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_SERVICE_ORDER_INFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
    }

    @Override
    public ServiceOrderInfoBean parseData(String result) {
        return serviceOrderInfoBean;
    }

    ServiceOrderInfoBean serviceOrderInfoBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceOrderInfoBean = gson.fromJson(result, ServiceOrderInfoBean.class);
        if (serviceOrderInfoBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
