package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 二、[需求]-查看需求详情
 * <p/>
 * <p/>
 * Created by zhouyifeng on 2016/11/13.
 */
public class DemandDetailProtocol extends BaseProtocol<DemandDetailBean> {

    String id;

    public DemandDetailProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DEMAND_DETAIL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public DemandDetailBean parseData(String result) {
        return demandDetailBean;
    }

    DemandDetailBean demandDetailBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        demandDetailBean = gson.fromJson(result, DemandDetailBean.class);
        if (demandDetailBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
