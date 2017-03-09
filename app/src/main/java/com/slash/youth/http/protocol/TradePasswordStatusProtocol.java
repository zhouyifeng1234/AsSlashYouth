package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;

import org.xutils.http.RequestParams;

/**
 * 七、[设置]-判断是否有交易密码
 * <p/>
 * Created by zhouyifeng on 2016/11/14.
 */
public class TradePasswordStatusProtocol extends BaseProtocol<CommonResultBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_TRADE_PASSWORD_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public CommonResultBean parseData(String result) {
        Gson gson = new Gson();
        CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
        return commonResultBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
