package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 四、[我的收藏]-是否收藏某任务
 * <p/>
 * Created by zhouyifeng on 2017/1/5.
 */
public class CollectionStatusProtocol extends BaseProtocol<CommonResultBean> {

    private String type;//1需求 2服务
    private String tid;//需求或者服务ID

    public CollectionStatusProtocol(String type, String tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.IS_COLLECT_TASK;
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
