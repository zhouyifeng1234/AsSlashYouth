package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/2.
 */
public class DeleteMyPublishTaskItemProtocol extends BaseProtocol<SetBean> {
    private int type;
    private long tid;


    public DeleteMyPublishTaskItemProtocol(int type, long tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MANAGE_PUBLISH_DELETE;
    }

    @Override
    public void addRequestParams(RequestParams params) {

        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("tid", String.valueOf(tid));
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
