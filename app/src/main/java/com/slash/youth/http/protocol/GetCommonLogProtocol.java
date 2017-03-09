package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonLogList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 一、[通用任务日志]-需求流程日志接口
 * Created by zhouyifeng on 2017/2/5.
 */
public class GetCommonLogProtocol extends BaseProtocol<CommonLogList> {

    private String tid;//需求or服务 任务ID
    private String type;//1需求 or 2 服务
    private String roleid;//1发布者 2预约者（抢单者）

    public GetCommonLogProtocol(String tid, String type, String roleid) {
        this.tid = tid;
        this.type = type;
        this.roleid = roleid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_COMMON_LOG;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("type", type);
        params.addBodyParameter("roleid", roleid);
    }

    @Override
    public CommonLogList parseData(String result) {
        return commonLogList;
    }

    CommonLogList commonLogList;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonLogList = gson.fromJson(result, CommonLogList.class);
        if (commonLogList.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
