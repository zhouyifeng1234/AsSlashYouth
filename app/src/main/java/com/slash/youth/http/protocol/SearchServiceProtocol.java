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
    String uid;

    public SearchServiceProtocol(String uid) {
        this.uid= uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEND_PHONE_VERIFICATION_CODE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(uid);
        params.addBodyParameter("uid", uid);
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
