package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.FriendStatusBean;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/21.
 */
public class CheckFriendApplicationProtocol  extends BaseProtocol<FriendStatusBean> {
  private long uid;

    public CheckFriendApplicationProtocol(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CHECK_FRIEND_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(uid));

    }

    @Override
    public FriendStatusBean parseData(String result) {
        Gson gson = new Gson();
        FriendStatusBean friendStatusBean = gson.fromJson(result, FriendStatusBean.class);
        return friendStatusBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
