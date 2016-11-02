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
    int isauth;
    int star;

    public SearchUserProtocol(String tag, int isauth, int star) {
        this.tag = tag;
        this.isauth=isauth;
        this.star=star;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_USER;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v("这是什么情况     "+tag+"  "+isauth+"   "+star);
        params.addBodyParameter("tag", tag);
        params.addBodyParameter("isauth", String.valueOf(isauth));
        params.addBodyParameter("star", String.valueOf(star));
    }

    @Override
    public SearchUserBean parseData(String result) {
        Gson gson = new Gson();
        SearchUserBean searchUserBean = gson.fromJson(result, SearchUserBean.class);
        LogKit.d("dsklfh 了解多少发的连接上看见=="+result);


        return searchUserBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
