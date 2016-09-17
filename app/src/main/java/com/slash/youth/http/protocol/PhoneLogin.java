package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PhoneLogin extends BaseProtocol<String> {

    String phone;//手机号
    String pin;//手机验证码
    String _3pToken;//可选。第三方token，格式为：第三方token+“&”+第三方uid+“&”+第三方平台类型
    String userInfo;//userInfo

    public PhoneLogin(String phone, String pin, String _3pToken, String userInfo) {
        this.phone = phone;
        this.pin = pin;
        this._3pToken = _3pToken;
        this.userInfo = userInfo;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.PHONE_NUMBER_LOGIN;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("pin", pin);
        params.addBodyParameter("3pToken", _3pToken);
        params.addBodyParameter("userInfo", userInfo);
    }

    @Override
    public String parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
