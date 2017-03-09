package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyHomeInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/22.
 * 十三、[用戶信息]-我的首页数据 （可以获得手机号）
 */
public class LoginUserHomeInfoProtocol extends BaseProtocol<MyHomeInfoBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_INFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public MyHomeInfoBean parseData(String result) {
        return myHomeInfoBean;
    }

    MyHomeInfoBean myHomeInfoBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        myHomeInfoBean = gson.fromJson(result, MyHomeInfoBean.class);
        if (myHomeInfoBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
