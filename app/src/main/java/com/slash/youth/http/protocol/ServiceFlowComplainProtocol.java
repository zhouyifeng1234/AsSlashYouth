package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ServiceFlowComplainResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十一、[服务]-服务方不同意退款并申请平台介入
 * <p/>
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceFlowComplainProtocol extends BaseProtocol<ServiceFlowComplainResultBean> {

    private String soid;// 服务订单ID
    private String remark;//申请平台介入原因  这个字段好像可以不写

    public ServiceFlowComplainProtocol(String soid, String remark) {
        this.soid = soid;
        this.remark = remark;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_FLOW_INTERVENTION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
        params.addBodyParameter("remark", remark);
    }

    @Override
    public ServiceFlowComplainResultBean parseData(String result) {
        return serviceFlowComplainResultBean;
    }

    ServiceFlowComplainResultBean serviceFlowComplainResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        serviceFlowComplainResultBean = gson.fromJson(result, ServiceFlowComplainResultBean.class);
        if (serviceFlowComplainResultBean.rescode == 0 && serviceFlowComplainResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
