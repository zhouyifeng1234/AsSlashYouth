package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/2.
 */
public class DeleteMyCollectionItemProtocol extends BaseProtocol<SetBean> {
    private int type;
    private long tid;

    public DeleteMyCollectionItemProtocol(int type, long tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_DELETE_COLLECTION_ITEM;
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
