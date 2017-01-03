package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2017/1/3.
 * <p/>
 * 四、[我发布的管理]-上下架-通过需求or服务ID
 */
public class UpAndDownTaskProtocol extends BaseProtocol<CommonResultBean> {

    private String tid;//需求或者服务ID
    private String type;//类型 1需求 2服务
    private String action;//1上架 0下架

    public UpAndDownTaskProtocol(String tid, String type, String action) {
        this.tid = tid;
        this.type = type;
        this.action = action;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.UP_AND_DOWN_TASK;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("type", type);
        params.addBodyParameter("action", action);
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
