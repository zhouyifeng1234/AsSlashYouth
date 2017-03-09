package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/20.
 */
public class CollectTaskProtocol extends BaseProtocol<CommonResultBean> {
    private String tid;
    private String type;

    public CollectTaskProtocol(String tid, String type) {
        this.tid = tid;
        this.type = type;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_ADD_COLLECTION_ITEM;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", type);
        params.addBodyParameter("tid", tid);
    }

    @Override
    public CommonResultBean parseData(String result) {
        return commonResultBean;
    }

    CommonResultBean commonResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonResultBean = gson.fromJson(result, CommonResultBean.class);
        if (commonResultBean.rescode == 0 && commonResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
