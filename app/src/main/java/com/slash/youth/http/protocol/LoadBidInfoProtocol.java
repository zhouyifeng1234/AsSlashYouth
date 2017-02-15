package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandBidInfoBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 五、[需求]-加载抢单信息(用于抢单人加载自己的抢单信息)
 * <p/>
 * Created by zhouyifeng on 2017/2/15.
 */
public class LoadBidInfoProtocol extends BaseProtocol<DemandBidInfoBean> {

    private String id;// 需求ID
    private String uid;//抢单用户ID

    public LoadBidInfoProtocol(String id, String uid) {
        this.id = id;
        this.uid = uid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_LOAD_BID_FINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("uid", uid);
    }

    @Override
    public DemandBidInfoBean parseData(String result) {
        return demandBidInfoBean;
    }

    DemandBidInfoBean demandBidInfoBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        demandBidInfoBean = gson.fromJson(result, DemandBidInfoBean.class);
        if (demandBidInfoBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
