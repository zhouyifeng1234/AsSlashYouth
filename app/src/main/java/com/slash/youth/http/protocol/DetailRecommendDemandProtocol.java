package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DetailRecommendDemandList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2017/1/4.
 */
public class DetailRecommendDemandProtocol extends BaseProtocol<DetailRecommendDemandList> {

    private String id;//服务明细ID
    private String limit;//一次拉取限制

    public DetailRecommendDemandProtocol(String id, String limit) {
        this.id = id;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DETAIL_RECOMMEND_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public DetailRecommendDemandList parseData(String result) {
        return detailRecommendDemandList;
    }

    DetailRecommendDemandList detailRecommendDemandList;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        detailRecommendDemandList = gson.fromJson(result, DetailRecommendDemandList.class);
        if (detailRecommendDemandList.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
