package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/18.
 */
public class CheckoutAuthProcessProtocol extends BaseProtocol<SetBean> {

    public CheckoutAuthProcessProtocol() {

    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_CHECKOUT_AUTH_SRATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
