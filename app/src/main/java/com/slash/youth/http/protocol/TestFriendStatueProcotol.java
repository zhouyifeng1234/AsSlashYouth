package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.FansBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/29.
 */
public class TestFriendStatueProcotol extends BaseProtocol<FansBean> {
    private long uid;

    public TestFriendStatueProcotol(long uid) {
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.TEST_FRIEND_STATUE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("touid", String.valueOf(uid));

    }

    @Override
    public FansBean parseData(String result) {
        Gson gson = new Gson();
        FansBean fansBean= gson.fromJson(result, FansBean.class);
        return fansBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
