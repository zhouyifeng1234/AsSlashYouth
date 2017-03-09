package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.BannerConfigBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/8.
 */
public class FirstPagerAdvertisementProtocol extends BaseProtocol<BannerConfigBean> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_BANNER_CONFIG;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public BannerConfigBean parseData(String result) {
        Gson gson = new Gson();
        BannerConfigBean bannerConfigBean = gson.fromJson(result, BannerConfigBean.class);
        return bannerConfigBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
