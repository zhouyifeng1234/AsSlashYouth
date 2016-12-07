package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/29.
 */
public class CareTAProtocol  extends BaseProtocol<SetBean>{
    private long uid;

    public CareTAProtocol(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CARE_TA;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(uid));
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
