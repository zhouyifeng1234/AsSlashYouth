package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十六、[需求]-需求方获取意向单列表
 * <p/>
 * Created by zhouyifeng on 2016/11/6.
 */
public class DemandPurposeProtocol extends BaseProtocol<DemandPurposeListBean> {

    String id;// 需求ID

    public DemandPurposeProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DEMAND_PURPOSE_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public DemandPurposeListBean parseData(String result) {
        return demandPurposeListBean;
    }

    DemandPurposeListBean demandPurposeListBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        demandPurposeListBean = gson.fromJson(result, DemandPurposeListBean.class);
        if (demandPurposeListBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
