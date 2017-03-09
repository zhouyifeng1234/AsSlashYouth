package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.NewDemandAandServiceBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/12.
 */
public class NewDemandAndServiceProtocol extends BaseProtocol<NewDemandAandServiceBean> {
    private long uid;
    private int offset;
    private int limit;
    private int anonymity;

    public NewDemandAndServiceProtocol(long uid, int offset, int limit,int anonymity) {
        this.uid = uid;
        this.offset = offset;
        this.limit = limit;
        this.anonymity = anonymity;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.NEW_DEMAND_ANDSERVICE_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {

        if(uid>0){
            params.addBodyParameter("uid", String.valueOf(uid));
        }

        if(offset>0 && offset==0){
            params.addBodyParameter("offset",String.valueOf(offset));
        }
        if(limit>0&&limit<=20){
            params.addBodyParameter("limit", String.valueOf(limit));
        }
        params.addBodyParameter("anonymity", String.valueOf(anonymity));
    }

    @Override
    public NewDemandAandServiceBean parseData(String result) {
        Gson gson = new Gson();
        NewDemandAandServiceBean newDemandAandServiceBean = gson.fromJson(result, NewDemandAandServiceBean.class);
        return newDemandAandServiceBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
