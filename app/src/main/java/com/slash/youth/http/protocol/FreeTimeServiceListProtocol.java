package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/8.
 */
public class FreeTimeServiceListProtocol extends BaseProtocol<FreeTimeServiceBean> {

    private int limit;

    public FreeTimeServiceListProtocol(int limit) {
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.FIRST_PAGER_SERVICE_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {

        if (limit>0&&limit<=10) {
            params.addBodyParameter("limit", String.valueOf(limit));
        }
    }

    @Override
    public FreeTimeServiceBean parseData(String result) {

        Gson gson = new Gson();
        FreeTimeServiceBean freeTimeServiceBean = gson.fromJson(result, FreeTimeServiceBean.class);
        return freeTimeServiceBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
