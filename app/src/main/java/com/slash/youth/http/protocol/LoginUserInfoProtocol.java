package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/21.
 */
public class LoginUserInfoProtocol extends BaseProtocol<UserInfoBean> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public UserInfoBean parseData(String result) {
        return userInfoBean;
    }

    UserInfoBean userInfoBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        userInfoBean = gson.fromJson(result, UserInfoBean.class);
        if (userInfoBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
