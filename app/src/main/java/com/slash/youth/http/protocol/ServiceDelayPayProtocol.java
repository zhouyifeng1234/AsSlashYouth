package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 预约服务者 延期支付
 * <p/>
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceDelayPayProtocol extends BaseProtocol<CommonResultBean> {

    private String soid;
    private String fid;

    public ServiceDelayPayProtocol(String soid, String fid) {
        this.soid = soid;
        this.fid = fid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_DELAY_PAY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
        params.addBodyParameter("fid", fid);
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
