package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 三、[我的收藏]-取消收藏
 * Created by zhouyifeng on 2017/1/5.
 */
public class CancelCollectionProtocol extends BaseProtocol<CommonResultBean> {

    private String type;//1需求 2服务
    private String tid;//需求or服务ID

    public CancelCollectionProtocol(String type, String tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_DELETE_COLLECTION_ITEM;
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
