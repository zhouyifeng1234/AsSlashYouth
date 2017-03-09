package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/7.
 */
public class GetUserInfoProtocol extends BaseProtocol<UserInfoItemBean> {


    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public UserInfoItemBean parseData(String result) {
        Gson gson = new Gson();
        UserInfoItemBean userInfoItemBean = gson.fromJson(result, UserInfoItemBean.class);
        return userInfoItemBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
