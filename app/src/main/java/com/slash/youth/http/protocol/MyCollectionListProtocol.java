package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/20.
 */
public class MyCollectionListProtocol extends BaseProtocol<MyCollectionBean> {
    private int offset;
    private int limit;
    private String url;

    public MyCollectionListProtocol(int offset, int limit,String url) {
        this.offset = offset;
        this.limit = limit;
        this.url = url;
    }

    @Override
    public String getUrlString() {
        return url;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public MyCollectionBean parseData(String result) {

        Gson gson = new Gson();

        MyCollectionBean myCollectionBean = gson.fromJson(result, MyCollectionBean.class);

        return myCollectionBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
