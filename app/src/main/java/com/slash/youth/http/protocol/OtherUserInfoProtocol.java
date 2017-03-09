package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/1.
 */
public class OtherUserInfoProtocol extends BaseProtocol<UserInfoBean> {
    String uid;
    String isvisitor;

    public OtherUserInfoProtocol(String uid, String isvisitor) {
        this.uid = uid;
        this.isvisitor = isvisitor;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("isvisitor", isvisitor);
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
