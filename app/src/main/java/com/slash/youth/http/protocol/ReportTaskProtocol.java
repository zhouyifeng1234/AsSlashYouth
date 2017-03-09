package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * 一、[举报]-举报一个需求或服务
 * <p>
 * Created by zhouyifeng on 2017/3/9.
 */
public class ReportTaskProtocol extends BaseProtocol<CommonResultBean> {

    private String tid;//需求或服务ID
    private String type;//需求1 服务2
    private String reason;//举报原因
    private String detail;//举报原因详情

    public ReportTaskProtocol(String tid, String type, String reason, String detail) {
        this.tid = tid;
        this.type = type;
        this.reason = reason;
        this.detail = detail;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.REPORT_DEMAND_SERVICE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("type", type);
        params.addBodyParameter("reason", reason);
        params.addBodyParameter("detail", detail);
        LogKit.v("tid:" + tid + " type:" + type + " reason:" + reason + " detail:" + detail);
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
