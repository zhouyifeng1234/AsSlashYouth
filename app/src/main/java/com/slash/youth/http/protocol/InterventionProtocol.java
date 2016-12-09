package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.InterventionBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十九、[需求]-服务方不同意退款并申请平台介入
 * Created by zhouyifeng on 2016/11/3.
 */
public class InterventionProtocol extends BaseProtocol<InterventionBean> {

    String id;//需求ID
    String remark;//申请平台介入原因 这个字段好像不用填，暂时先留着

    public InterventionProtocol(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_INTERVENTION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("remark", remark);
    }

    @Override
    public InterventionBean parseData(String result) {
        return interventionBean;
    }

    InterventionBean interventionBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        interventionBean = gson.fromJson(result, InterventionBean.class);
        if (interventionBean.rescode == 0) {
            if (interventionBean.data.status == 1) {
                return true;
            }
        }
        return false;
    }
}
