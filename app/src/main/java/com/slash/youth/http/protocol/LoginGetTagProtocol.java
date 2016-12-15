package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/14.
 */
public class LoginGetTagProtocol extends BaseProtocol<String> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGNI_GET_TAG;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public String parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
