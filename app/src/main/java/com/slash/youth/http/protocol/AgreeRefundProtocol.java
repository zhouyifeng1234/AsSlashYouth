package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十八、[需求]-服务方确认同意退款
 * Created by zhouyifeng on 2016/11/3.
 */
public class AgreeRefundProtocol extends BaseProtocol<CommonResultBean> {

    String id;//需求ID

    public AgreeRefundProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_PARTY_AGREE_REFUND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
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
