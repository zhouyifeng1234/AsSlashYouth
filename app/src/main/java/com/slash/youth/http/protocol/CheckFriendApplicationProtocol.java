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
    private int offset;
    private int limit;

    public CheckFriendApplicationProtocol(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CHECK_FRIEND_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if(offset>=0){
            params.addBodyParameter("offset", String.valueOf(offset));
        }
      if(limit>0&&limit<=20){
          params.addBodyParameter("limit", String.valueOf(limit));
      }
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
