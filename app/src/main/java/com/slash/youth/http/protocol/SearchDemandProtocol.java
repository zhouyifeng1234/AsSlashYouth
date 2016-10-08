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
    String uid;
    String extra;

    public SearchDemandProtocol(String tag,String extra) {
        this.uid= tag;
        this.extra=extra;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(uid+extra);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("extra", extra);
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
