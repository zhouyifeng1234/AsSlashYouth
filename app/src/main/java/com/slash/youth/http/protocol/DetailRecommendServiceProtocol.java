package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DetailRecommendServiceList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2017/1/4.
 */
public class DetailRecommendServiceProtocol extends BaseProtocol<DetailRecommendServiceList> {

    private String id;//服务明细ID
    private String limit;//一次拉取限制

    public DetailRecommendServiceProtocol(String id, String limit) {
        this.id = id;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DETAIL_RECOMMEND_SERVICE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public DetailRecommendServiceList parseData(String result) {
        return detailRecommendServiceList;
    }

    DetailRecommendServiceList detailRecommendServiceList;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        detailRecommendServiceList = gson.fromJson(result, DetailRecommendServiceList.class);
        if (detailRecommendServiceList.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
