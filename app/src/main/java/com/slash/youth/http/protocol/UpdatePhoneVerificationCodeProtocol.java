package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/9/10.
 */
public class UpdatePhoneVerificationCodeProtocol extends BaseProtocol<SendPinResultBean> {
   private String mPhoneNum;
   private String  pin;

    public UpdatePhoneVerificationCodeProtocol(String phoneNum,String pin) {
        this.mPhoneNum = phoneNum;
        this.pin = pin;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.UPDATE_VERIFICATION_PHONE_VERIFICATION_CODE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("phone", mPhoneNum);
        params.addBodyParameter("pin", pin);

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
