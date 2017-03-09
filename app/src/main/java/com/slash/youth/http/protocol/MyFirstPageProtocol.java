package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/9.
 */
public class MyFirstPageProtocol extends BaseProtocol<MyFirstPageBean> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_INFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public MyFirstPageBean parseData(String result) {
        Gson gson = new Gson();
        MyFirstPageBean myFirstPageBean = gson.fromJson(result, MyFirstPageBean.class);
        return myFirstPageBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
