package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/8.
 */
public class FreeTimeDemandProtocol extends BaseProtocol<FreeTimeDemandBean>{
    private int limit;

    public FreeTimeDemandProtocol( int limit) {
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.FIRST_PAGER_DEMAND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if (limit>0&&limit<=10) {
            params.addBodyParameter("limit", String.valueOf(limit));
        }
    }

    @Override
    public FreeTimeDemandBean parseData(String result) {
        Gson gson = new Gson();
        FreeTimeDemandBean freeTimeDemandBean = gson.fromJson(result, FreeTimeDemandBean.class);
        return freeTimeDemandBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
