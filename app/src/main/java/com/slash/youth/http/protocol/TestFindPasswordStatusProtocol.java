package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 七、[设置]-判断是否找回交易密码
 * <p/>
 * Created by ZSS on 2016/11/14.
 */
public class TestFindPasswordStatusProtocol extends BaseProtocol<CommonResultBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.TEST_FIND_PASSWORD_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public CommonResultBean parseData(String result) {
        Gson gson = new Gson();
        CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
        return commonResultBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
