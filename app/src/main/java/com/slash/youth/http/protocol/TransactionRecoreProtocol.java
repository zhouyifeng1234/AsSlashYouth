package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/10.
 */
public class TransactionRecoreProtocol extends BaseProtocol<TransactionRecoreBean> {
    private int offset;
    private int limit;

    public TransactionRecoreProtocol(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_TRANSACTIONRECORE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if(offset>=0){
            params.addBodyParameter("offset", String.valueOf(offset));
        }
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public TransactionRecoreBean parseData(String result) {
        Gson gson = new Gson();
        TransactionRecoreBean transactionRecoreBean = gson.fromJson(result, TransactionRecoreBean.class);
        return transactionRecoreBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
