package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.HomeRecommendList2;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * V1.1版新增  四、[推荐]-更多服务推荐列表
 * <p/>
 * Created by zhouyifeng on 2017/3/3.
 */
public class RecommendServiceMore2Protocol extends BaseProtocol<HomeRecommendList2> {

    String rec_offset;//推荐分页offset
    String rec_limit;//推荐分页limit
    String rad_offset;//随机分页offset
    String rad_limit;//随机分页limit

    public RecommendServiceMore2Protocol(String rec_offset, String rec_limit, String rad_offset, String rad_limit) {
        this.rec_offset = rec_offset;
        this.rec_limit = rec_limit;
        this.rad_offset = rad_offset;
        this.rad_limit = rad_limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_RECOMMEND_SERVICE_MORE2;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("rec_offset", rec_offset);
        params.addBodyParameter("rec_limit", rec_limit);
        params.addBodyParameter("rad_offset", rad_offset);
        params.addBodyParameter("rad_limit", rad_limit);
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
