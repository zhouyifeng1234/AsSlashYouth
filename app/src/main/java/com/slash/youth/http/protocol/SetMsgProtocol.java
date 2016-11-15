package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetMsgBean;
import com.slash.youth.domain.SetTimeBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/11.
 */
public class SetMsgProtocol extends BaseProtocol<SetMsgBean> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_MSG_GET;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public SetMsgBean parseData(String result) {
        Gson gson = new Gson();
        SetMsgBean setMsgBean = gson.fromJson(result, SetMsgBean.class);
        return setMsgBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
