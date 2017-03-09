package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.HomeTagInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 二、[推广]-首页Tag配置信息接口
 * <p/>
 * Created by zhouyifeng on 2017/2/27.
 */
public class HomeTagConfigProtocol extends BaseProtocol<HomeTagInfoBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.HOME_TAG_CONFIG;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public HomeTagInfoBean parseData(String result) {
        Gson gson = new Gson();
        HomeTagInfoBean homeTagInfoBean = gson.fromJson(result, HomeTagInfoBean.class);
        return homeTagInfoBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
