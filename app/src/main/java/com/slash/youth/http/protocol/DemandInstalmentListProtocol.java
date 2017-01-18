package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandInstalmentListBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 获取需求的分期信息
 * <p/>
 * Created by zhouyifeng on 2017/1/18.
 */
public class DemandInstalmentListProtocol extends BaseProtocol<DemandInstalmentListBean> {

    private String id;//需求ID

    public DemandInstalmentListProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_DEMAND_INSTALMENT_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public DemandInstalmentListBean parseData(String result) {
        return demandInstalmentListBean;
    }

    DemandInstalmentListBean demandInstalmentListBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        demandInstalmentListBean = gson.fromJson(result, DemandInstalmentListBean.class);
        if (demandInstalmentListBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
