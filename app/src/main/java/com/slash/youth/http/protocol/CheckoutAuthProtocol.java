package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/18.
 */
public class CheckoutAuthProtocol extends BaseProtocol<SetBean> {
    private int type;
    private int cardtype;
    private String url;

    public CheckoutAuthProtocol(int type, int cardtype, String url) {
        this.type = type;
        this.cardtype = cardtype;
        this.url = url;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_CHECKOUT_AUTH;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("cardtype", String.valueOf(cardtype));
        params.addBodyParameter("url",url);
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
