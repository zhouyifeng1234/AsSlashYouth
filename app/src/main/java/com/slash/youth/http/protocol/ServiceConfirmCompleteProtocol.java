package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 八、[服务]-需求方确认完成任务  (服务流程中)
 * <p/>
 * Created by zhouyifeng on 2016/12/7.
 */
public class ServiceConfirmCompleteProtocol extends BaseProtocol<CommonResultBean> {

    private String soid;
    private String fid;

    public ServiceConfirmCompleteProtocol(String soid, String fid) {
        this.soid = soid;
        this.fid = fid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_CONFIRM_COMPLETE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("soid", soid);
        params.addBodyParameter("fid", fid);
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
