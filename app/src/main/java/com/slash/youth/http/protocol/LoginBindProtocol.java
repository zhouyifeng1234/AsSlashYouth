package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.LoginBindBean;
import com.slash.youth.domain.UserSkillLabelBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/13.
 */
public class LoginBindProtocol extends  BaseProtocol<LoginBindBean> {
    private String token;
    private String uid;
    private byte loginPlatform;

    public LoginBindProtocol(String token, String uid, byte loginPlatform) {
        this.token = token;
        this.uid = uid;
        this.loginPlatform = loginPlatform;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGIN_BINDING;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("3pToken",token);
        params.addBodyParameter("3pUid",uid);
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
