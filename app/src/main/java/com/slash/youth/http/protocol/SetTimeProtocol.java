package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetTimeBean;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/11.
 */
public class SetTimeProtocol extends BaseProtocol<SetTimeBean> {


    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_TIME_GET;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public SetTimeBean parseData(String result) {
        Gson gson = new Gson();
        SetTimeBean setTimeBean = gson.fromJson(result, SetTimeBean.class);
        return setTimeBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
