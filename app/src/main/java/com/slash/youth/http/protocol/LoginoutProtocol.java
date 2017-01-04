package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.RecodeBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/16.
 */
public class LoginoutProtocol extends BaseProtocol<RecodeBean>{
    private String token;

    public LoginoutProtocol(String token) {
        this.token = token;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_LOGOUT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("token",token);
    }

    @Override
    public RecodeBean parseData(String result) {
        Gson gson = new Gson();
        RecodeBean recodeBean = gson.fromJson(result, RecodeBean.class);
        return recodeBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
