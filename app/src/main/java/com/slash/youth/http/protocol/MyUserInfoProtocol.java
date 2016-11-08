package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/7.
 */
public class MyUserInfoProtocol extends BaseProtocol<UserInfoItemBean> {
    private long uid;

    public MyUserInfoProtocol(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(uid));
        params.addHeader("uid", String.valueOf(10001));

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
