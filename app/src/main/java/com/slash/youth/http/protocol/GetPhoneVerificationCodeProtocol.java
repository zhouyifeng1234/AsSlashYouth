package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/10.
 */
public class GetPhoneVerificationCodeProtocol extends BaseProtocol<String> {
    String mPhoneNum;

    public GetPhoneVerificationCodeProtocol(String phoneNum) {
        this.mPhoneNum = phoneNum;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEND_PHONE_VERIFICATION_CODE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(mPhoneNum);
        params.addBodyParameter("phone", mPhoneNum);
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
