package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.StringUtils;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/10.
 */
public class GetPhoneVerificationCodeProtocol extends BaseProtocol<SendPinResultBean> {
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
    public SendPinResultBean parseData(String result) {
        return sendPinResultBean;
    }

    SendPinResultBean sendPinResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        sendPinResultBean = gson.fromJson(result, SendPinResultBean.class);
        if (sendPinResultBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
