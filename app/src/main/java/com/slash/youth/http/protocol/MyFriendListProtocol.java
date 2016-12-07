package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/27.
 */
public class MyFriendListProtocol extends BaseProtocol<MyFriendListBean> {
     private int offset;
    private int limit;

    public MyFriendListProtocol(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_FRIEND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public MyFriendListBean parseData(String result) {
        Gson gson = new Gson();
        MyFriendListBean myFriendListBean = gson.fromJson(result, MyFriendListBean.class);
        return myFriendListBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
