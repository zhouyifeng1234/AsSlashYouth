package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DelayPayBean;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/10.
 */
public class MyAccountProtocol extends BaseProtocol<MyAccountBean>  {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_ACCOUNT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public MyAccountBean parseData(String result) {


        Gson gson = new Gson();

        MyAccountBean myAccountBean = gson.fromJson(result, MyAccountBean.class);

        return myAccountBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
