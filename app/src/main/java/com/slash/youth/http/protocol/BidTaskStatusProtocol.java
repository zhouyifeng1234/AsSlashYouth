package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 四、[需求]-是否预约过某服务或者抢单过某需求
 * Created by zhouyifeng on 2017/1/5.
 */
public class BidTaskStatusProtocol extends BaseProtocol<CommonResultBean> {

    private String type;//1需求 2服务
    private String tid;//任务ID

    public BidTaskStatusProtocol(String type, String tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_BID_TASK_STATUS;
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
        if (commonResultBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
