package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.RecommendFriendBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserSkillLabelBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/27.
 */
public class RecommendFriendListProtovol extends BaseProtocol<RecommendFriendBean> {
    private int limit;

    public RecommendFriendListProtovol(int limit) {
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.RECOMMONEND_FRIEND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public RecommendFriendBean parseData(String result) {
        Gson gson = new Gson();
        RecommendFriendBean recommendFriendBean = gson.fromJson(result, RecommendFriendBean.class);
        return recommendFriendBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
