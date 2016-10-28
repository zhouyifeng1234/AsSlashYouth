package com.slash.youth.http.protocol;

import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/10/28.
 */
public class GetMyTaskListProtocol extends BaseProtocol<MyTaskBean> {

    int type;
    int offset;
    int limit;

    public GetMyTaskListProtocol(int type, int offset, int limit) {
        this.type = type;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_MY_TASK_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("offset", offset + "");
        params.addBodyParameter("limit", limit + "");
    }

    @Override
    public MyTaskBean parseData(String result) {
        return null;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return false;
    }
}
