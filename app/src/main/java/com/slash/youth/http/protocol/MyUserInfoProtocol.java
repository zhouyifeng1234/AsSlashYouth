package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.OtherInfoBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/7.
 */
public class MyUserInfoProtocol extends BaseProtocol<OtherInfoBean> {
    private long uid;
    private int anonymity;

    public MyUserInfoProtocol(long uid,int anonymity) {
        this.uid = uid;
        this.anonymity = anonymity;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(uid));
        params.addBodyParameter("anonymity", String.valueOf(anonymity));
    }

    @Override
    public OtherInfoBean parseData(String result) {
        Gson gson = new Gson();
        OtherInfoBean otherInfoBean = gson.fromJson(result, OtherInfoBean.class);
        return otherInfoBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
