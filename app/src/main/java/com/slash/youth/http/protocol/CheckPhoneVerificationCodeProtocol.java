package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/10.
 */
public class CheckPhoneVerificationCodeProtocol extends BaseProtocol<String> {

    private String mPhoneNum;
    private String mPin;

    /**
     * @param phoneNum 手机号码
     * @param pin      手机上收到的短信验证码
     */
    public CheckPhoneVerificationCodeProtocol(String phoneNum, String pin) {
        this.mPhoneNum = phoneNum;
        this.mPin = pin;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.VERIFICATION_PHONE_VERIFICATION_CODE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("phone", mPhoneNum);
        params.addBodyParameter("pin", mPin);
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
