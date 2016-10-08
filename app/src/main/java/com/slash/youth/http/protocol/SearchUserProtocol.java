package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.SearchUserBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.
 */
public class SearchUserProtocol extends BaseProtocol<SearchUserBean> {
    String tag;
    String auth;
    String star;
    String activeness;

    public SearchUserProtocol(String tag, String auth, String star, String activeness) {
        this.tag = tag;
        this.auth=auth;
        this.star=star;
        this.activeness=activeness;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_USER;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(tag+auth+star+activeness);
        params.addBodyParameter("tag", tag);
        params.addBodyParameter("auth", auth);
        params.addBodyParameter("star", star);
        params.addBodyParameter("activeness", activeness);
    }

    @Override
    public SearchUserBean parseData(String result) {
        Gson gson = new Gson();
        SearchUserBean searchUserBean = gson.fromJson(result, SearchUserBean.class);
        return searchUserBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
