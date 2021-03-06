package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十一、[需求]-服务方完成任务
 * Created by zhouyifeng on 2016/10/8.
 */
public class ServicePartyCompleteProtocol extends BaseProtocol<CommonResultBean> {
    private String id;// 需求ID
    private String fid;//完成的是第几期

    public ServicePartyCompleteProtocol(String id, String fid) {
        this.id = id;
        this.fid = fid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_COMPLETE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("fid", fid);
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
            if (commonResultBean.data.status == 1) {
                return true;
            }
        }
        return false;
    }
}
