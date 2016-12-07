package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/30.
 */
public class CannelCareProtocol extends BaseProtocol<SetBean> {
    private long uid;

    public CannelCareProtocol(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CANNEL_CARE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("touid", String.valueOf(uid));

    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
