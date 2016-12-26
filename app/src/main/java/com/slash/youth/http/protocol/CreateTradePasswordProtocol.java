package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/25.
 */
public class CreateTradePasswordProtocol extends BaseProtocol<CommonResultBean> {

    private String pass;

    public CreateTradePasswordProtocol(String pass) {
        this.pass = pass;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CREATE_PASSWORD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("pass", pass);
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
