package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.LoginBindBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/13.
 */
public class LoginUnBindProtocol  extends BaseProtocol<LoginBindBean>{
    private byte loginPlatform;

    public LoginUnBindProtocol(byte loginPlatform) {
        this.loginPlatform = loginPlatform;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.UNBINDING;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("loginPlatform", String.valueOf(loginPlatform));
    }

    @Override
    public LoginBindBean parseData(String result) {
        Gson gson = new Gson();
        LoginBindBean loginBindBean = gson.fromJson(result, LoginBindBean.class);
        return loginBindBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
