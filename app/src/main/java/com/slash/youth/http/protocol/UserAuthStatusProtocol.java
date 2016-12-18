package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/18.
 */
public class UserAuthStatusProtocol extends BaseProtocol<CommonResultBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_USER_AUTH_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public CommonResultBean parseData(String result) {
        Gson gson = new Gson();
        CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
        return commonResultBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
