package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/16.
 */
public class LoginoutProtocol extends BaseProtocol{
  /*  private String token;

    public LoginoutProtocol(String token) {
        this.token = token;
    }*/

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_LOGOUT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
       // params.addBodyParameter("token",token);
    }

    @Override
    public Object parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
