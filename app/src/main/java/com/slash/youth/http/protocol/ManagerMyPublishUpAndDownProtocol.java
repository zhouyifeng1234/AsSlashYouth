package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/2.
 */
public class ManagerMyPublishUpAndDownProtocol extends BaseProtocol<SetBean> {
    private long id;
    private int action;

    public ManagerMyPublishUpAndDownProtocol(long id, int action) {
        this.id = id;
        this.action = action;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MANAGE_PUBLISH_UP_AND_DOWN;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", String.valueOf(id));
        params.addBodyParameter("action", String.valueOf(action));
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
