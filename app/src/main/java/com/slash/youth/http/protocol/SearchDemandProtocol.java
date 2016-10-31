package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.SearchDemandBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.
 */
public class SearchDemandProtocol extends BaseProtocol<SearchDemandBean> {
    String tag;
    int pattern;
    int isauth;


    public SearchDemandProtocol(String tag,int  pattern,int isauth) {
        this.tag= tag;
        this.pattern = pattern;
        this.isauth = isauth;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(tag+"  "+pattern+"  "+isauth);
        params.addBodyParameter("uid", tag);
        params.addBodyParameter("pattern", String.valueOf(pattern));
        params.addBodyParameter("isauth", String.valueOf(isauth));
    }

    @Override
    public SearchDemandBean parseData(String result) {
        Gson gson = new Gson();
        SearchDemandBean searchDemandBean = gson.fromJson(result, SearchDemandBean.class);
        return searchDemandBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
