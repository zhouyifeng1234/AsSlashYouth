package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/23.
 */
public class AddFriendProtocol extends BaseProtocol<CommonResultBean> {

    private String uid;
    private String extra;

    public AddFriendProtocol(String uid, String extra) {
        this.uid = uid;
        this.extra = extra;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.ADD_FRIEND_RELATION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("extra", extra);
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
