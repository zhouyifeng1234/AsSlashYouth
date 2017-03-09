package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.RecommendServiceUserBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/18.
 */
public class RecommendServiceUserProtocol extends BaseProtocol<RecommendServiceUserBean> {

    private String id; //需求详情ID
    private String limit; //一次拉取限制

    public RecommendServiceUserProtocol(String id, String limit) {
        this.id = id;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_RECOMMEND_SERVICE_USER;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public RecommendServiceUserBean parseData(String result) {
        return recommendServiceUserBean;
    }

    RecommendServiceUserBean recommendServiceUserBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        recommendServiceUserBean = gson.fromJson(result, RecommendServiceUserBean.class);
        if (recommendServiceUserBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
