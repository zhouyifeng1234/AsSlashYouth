package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 二十四、[需求]-查询是否延期支付过
 * <p/>
 * Created by zhouyifeng on 2017/1/18.
 */
public class DemandIsRectifyProtocol extends BaseProtocol<CommonResultBean> {

    private String id;//需求ID

    public DemandIsRectifyProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_IS_RECTIFY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
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
