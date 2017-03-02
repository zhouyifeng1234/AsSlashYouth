package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.HomeRecommendList2;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 一、[推荐]-首页需求推荐列表 v1.1首页需求推荐列表
 * <p/>
 * Created by zhouyifeng on 2017/3/2.
 */
public class RecommendDemand2Protocol extends BaseProtocol<HomeRecommendList2> {

    private String limit;//一次拉取限制 >0且<=20	 默认10

    public RecommendDemand2Protocol(String limit) {
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_RECOMMEND_DEMAND2;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("limit", limit);
    }

    @Override
    public HomeRecommendList2 parseData(String result) {
        return homeRecommendList2;
    }

    HomeRecommendList2 homeRecommendList2;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        homeRecommendList2 = gson.fromJson(result, HomeRecommendList2.class);
        if (homeRecommendList2.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
