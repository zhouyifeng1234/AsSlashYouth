package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchServiceBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchUserListProtocol extends BaseProtocol<SearchUserItemBean> {

    private String tag;
    private int isauth;
    private int sort;
    private int offset;
    private int limit;

    public SearchUserListProtocol(String tag,  int isauth,  int sort,  int offset, int limit) {
        this.tag = tag;
        this.isauth = isauth;
        this.sort = sort;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_USER;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tag",tag);
        if(isauth ==1||isauth ==0){
            params.addBodyParameter("isauth", String.valueOf(isauth));
        }
        if (offset>0||offset==0) {
            params.addBodyParameter("offset", String.valueOf(offset));
        }
        if (limit>0&&limit<=20) {
            params.addBodyParameter("limit", String.valueOf(limit));
        }
        if(sort ==1||sort==2||sort==3){
            params.addBodyParameter("sort", String.valueOf(sort));
        }
    }

    @Override
    public SearchUserItemBean parseData(String result) {
        Gson gson = new Gson();
        SearchUserItemBean searchUserItemBean = gson.fromJson(result, SearchUserItemBean.class);
        return searchUserItemBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
