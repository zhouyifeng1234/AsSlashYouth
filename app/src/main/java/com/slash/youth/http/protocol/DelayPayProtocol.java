package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DelayPayBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * [需求]-需求方要求延期付款接口
 *
 * Created by zhouyifeng on 2016/11/3.
 */
public class DelayPayProtocol extends BaseProtocol<DelayPayBean> {

    String id;// 需求ID
    String fid;

    public DelayPayProtocol(String id, String fid) {
        this.id = id;
        this.fid = fid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_DELAY_PAY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("fid", fid);
    }

    @Override
    public DelayPayBean parseData(String result) {
        Gson gson = new Gson();
        DelayPayBean delayPayBean = gson.fromJson(result, DelayPayBean.class);
        return delayPayBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
