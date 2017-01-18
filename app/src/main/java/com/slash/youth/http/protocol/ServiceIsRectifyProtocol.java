package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十七、[服务]-查询是否延期支付过
 * <p/>
 * Created by zhouyifeng on 2017/1/18.
 */
public class ServiceIsRectifyProtocol extends BaseProtocol<CommonResultBean> {

    private String soid;//服务订单ID

    public ServiceIsRectifyProtocol(String soid) {
        this.soid = soid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_IS_RECTIFY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
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
        if (commonResultBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
