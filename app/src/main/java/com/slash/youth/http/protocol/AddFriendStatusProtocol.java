package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/23.
 */
public class AddFriendStatusProtocol extends BaseProtocol<CommonResultBean> {

    private String uid;

    public AddFriendStatusProtocol(String uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_ADD_FRIEND_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", uid);
    }

    @Override
    public CommonResultBean parseData(String result) {
        Gson gson = new Gson();
        CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
        return commonResultBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        //这个接口不能在这里做判断
        return true;
    }
}
