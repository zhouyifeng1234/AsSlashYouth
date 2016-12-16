package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 登录后完善信息 设置头像
 * <p/>
 * Created by zhouyifeng on 2016/12/13.
 */
public class LoginSetAvatarProtocol extends BaseProtocol<CommonResultBean> {

    private String url;

    public LoginSetAvatarProtocol(String url) {
        this.url = url;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGIN_SET_AVATAR;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("url", url);
    }

    @Override
    public CommonResultBean parseData(String result) {
        return commonResultBean;
    }

    CommonResultBean commonResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonResultBean = gson.fromJson(result, CommonResultBean.class);
        if (commonResultBean.rescode == 0 && commonResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
