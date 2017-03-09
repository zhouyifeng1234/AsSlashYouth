package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceInstalmentListBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceInstalmentListProtocol extends BaseProtocol<ServiceInstalmentListBean> {

    private String soid;

    public ServiceInstalmentListProtocol(String soid) {
        this.soid = soid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_SETVICE_INSTALMENT_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
    }

    @Override
    public ServiceInstalmentListBean parseData(String result) {
        return serviceInstalmentListBean;
    }

    ServiceInstalmentListBean serviceInstalmentListBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceInstalmentListBean = gson.fromJson(result, ServiceInstalmentListBean.class);
        if (serviceInstalmentListBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
