package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchDemandBean;
import com.slash.youth.domain.SearchServiceBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.
 */
public class SearchServiceProtocol extends BaseProtocol<SearchServiceBean> {
    String tag;
    int pattern;
    int isauth;

    public SearchServiceProtocol(String tag,int pattern,int isauth) {
        this.tag= tag;
        this.pattern = pattern;
        this.isauth = isauth;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_SERVICE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(tag+"  "+pattern+"  "+isauth);
        params.addBodyParameter("tag", tag);
        params.addBodyParameter("pattern", String.valueOf(pattern));
        params.addBodyParameter("isauth", String.valueOf(isauth));
    }

    @Override
    public SearchServiceBean parseData(String result) {
        Gson gson = new Gson();
        SearchServiceBean searchServiceBean = gson.fromJson(result, SearchServiceBean.class);
        return searchServiceBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
