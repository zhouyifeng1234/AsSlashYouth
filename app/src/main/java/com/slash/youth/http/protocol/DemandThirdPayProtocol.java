package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ThirdPayChargeBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/23.
 */
public class DemandThirdPayProtocol extends BaseProtocol<ThirdPayChargeBean> {

    private String id;
    private String amount;
    private String channel;

    public DemandThirdPayProtocol(String id, String amount, String channel) {
        this.id = id;
        this.amount = amount;
        this.channel = channel;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_THIRD_PAY;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("amount", amount);
        params.addBodyParameter("channel", channel);
    }

    @Override
    public ThirdPayChargeBean parseData(String result) {
        return thirdPayChargeBean;
    }

    ThirdPayChargeBean thirdPayChargeBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        thirdPayChargeBean = gson.fromJson(result, ThirdPayChargeBean.class);
        if (thirdPayChargeBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
