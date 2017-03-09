package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十五、[需求]-设置需求备注信息
 * <p/>
 * Created by zhouyifeng on 2017/1/7.
 */
public class SetDemandRemarkProtocol extends BaseProtocol<CommonResultBean> {

    private String id;//需求ID
    private String remark;//需求备注 长度小于4096，该接口只能在服务方未确认前修改，服务方一旦确认该接口设置失效

    public SetDemandRemarkProtocol(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_DEMAND_REMARK;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("remark", remark);
    }

    @Override
    public CommonResultBean parseData(String result) {
        return commonResultBean;
    }

    CommonResultBean commonResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonResultBean = gson.fromJson(result, CommonResultBean.class);
        if (commonResultBean.rescode == 0 && commonResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
