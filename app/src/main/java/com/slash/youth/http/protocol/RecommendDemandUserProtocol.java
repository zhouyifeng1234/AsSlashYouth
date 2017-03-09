package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.RecommendDemandUserBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/18.
 */
public class RecommendDemandUserProtocol extends BaseProtocol<RecommendDemandUserBean> {

    private String id;//服务详情ID
    private String limit;//一次拉取限制

    public RecommendDemandUserProtocol(String id, String limit) {
        this.id = id;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_RECOMMEND_DEMAND_USER;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public RecommendDemandUserBean parseData(String result) {
        return recommendDemandUserBean;
    }

    RecommendDemandUserBean recommendDemandUserBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        recommendDemandUserBean = gson.fromJson(result, RecommendDemandUserBean.class);
        if (recommendDemandUserBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
