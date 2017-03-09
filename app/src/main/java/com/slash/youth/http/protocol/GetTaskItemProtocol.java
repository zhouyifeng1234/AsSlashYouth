package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/11/13.
 */
public class GetTaskItemProtocol extends BaseProtocol<MyTaskItemBean> {
    String tid;
    String type;
    String roleid;

    public GetTaskItemProtocol(String tid, String type, String roleid) {
        this.tid = tid;
        this.type = type;
        this.roleid = roleid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_MY_TASK_ITEM;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("type", type);
        params.addBodyParameter("roleid", roleid);
    }

    @Override
    public MyTaskItemBean parseData(String result) {
        return myTaskItemBean;
    }

    MyTaskItemBean myTaskItemBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        myTaskItemBean = gson.fromJson(result, MyTaskItemBean.class);
        if (myTaskItemBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
