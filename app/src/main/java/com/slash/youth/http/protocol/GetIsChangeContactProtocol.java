package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.IsChangeContactBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/22.
 */
public class GetIsChangeContactProtocol extends BaseProtocol<IsChangeContactBean> {
    private String uid;

    public GetIsChangeContactProtocol(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_IS_CHANGE_CONTACT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", uid);
    }

    @Override
    public IsChangeContactBean parseData(String result) {
        return isChangeContactBean;
    }

    IsChangeContactBean isChangeContactBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        isChangeContactBean = gson.fromJson(result, IsChangeContactBean.class);
        if (isChangeContactBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
