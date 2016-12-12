package com.slash.youth.http.protocol;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/8.
 */
public class FirstPagerAdvertisementProtocol extends BaseProtocol<String> {

    private String url;


    public FirstPagerAdvertisementProtocol(String url) {
        this.url = url;
    }

    @Override
    public String getUrlString() {
        return url;
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
