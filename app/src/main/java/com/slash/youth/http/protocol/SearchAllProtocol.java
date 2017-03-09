package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.
 */
public class SearchAllProtocol extends BaseProtocol<SearchAllBean> {
    String tag;
    private SearchAllBean searchAllBean;

    public SearchAllProtocol(String tag) {
        this.tag= tag;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_ALL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tag", tag);
    }

    @Override
    public SearchAllBean parseData(String result) {
        Gson gson = new Gson();
        searchAllBean = gson.fromJson(result, SearchAllBean.class);
        return searchAllBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
