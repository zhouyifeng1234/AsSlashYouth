package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 登录后完善信息 设置真实姓名
 * <p/>
 * Created by zhouyifeng on 2016/12/13.
 */
public class LoginSetRealnameProtocol extends BaseProtocol<CommonResultBean> {
    private String name;

    public LoginSetRealnameProtocol(String name) {
        this.name = name;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGIN_SET_REAL_NAME;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("name", name);
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
