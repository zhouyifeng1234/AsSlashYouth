package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 通过tid查询服务详情
 * <p/>
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyTaskServiceDetailProtocol extends BaseProtocol<ServiceDetailBean> {

    private String tid;
    private String roleid;

    public MyTaskServiceDetailProtocol(String tid, String roleid) {
        this.tid = tid;
        this.roleid = roleid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_MY_TASK_SERVICE_DEMAND_DETAIL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", "2");
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("roleid", roleid);
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
