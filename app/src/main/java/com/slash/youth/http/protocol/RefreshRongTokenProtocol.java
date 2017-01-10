package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.RongTokenBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 更新融云token
 * <p>
 * Created by zhouyifeng on 2017/1/8.
 */
public class RefreshRongTokenProtocol extends BaseProtocol<RongTokenBean> {

    private String uid;
    private String phone;

    public RefreshRongTokenProtocol(String uid, String phone) {
        this.uid = uid;
        this.phone = phone;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.REFRESH_RONG_TOKEN;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("phone", phone);
    }

    @Override
    public RongTokenBean parseData(String result) {
        return rongTokenBean;
    }

    RongTokenBean rongTokenBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        rongTokenBean = gson.fromJson(result, RongTokenBean.class);
        if (rongTokenBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
