package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/30.
 */
public class CancelDemandProtocol extends BaseProtocol<CommonResultBean> {
    private String cancelDemandId;

    public CancelDemandProtocol(String cancelDemandId) {
        this.cancelDemandId = cancelDemandId;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CANCEL_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", cancelDemandId);
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
