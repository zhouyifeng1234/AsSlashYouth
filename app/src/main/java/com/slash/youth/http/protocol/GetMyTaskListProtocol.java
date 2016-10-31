package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyTaskList;
import com.slash.youth.global.GlobalConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/10/28.
 */
public class GetMyTaskListProtocol extends BaseProtocol<MyTaskList> {

    int type;//列表类型 取值范围只能是: 我的任务列表类型定义 这些枚举
    int offset;//分页 >=0
    int limit;//分页 最大值20

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
    public MyTaskList parseData(String result) {
        Gson gson = new Gson();
        MyTaskList myTaskList = gson.fromJson(result, MyTaskList.class);
        return myTaskList;
    }

    @Override
    public boolean checkJsonResult(String result) {
        try {
            JSONObject joResult = new JSONObject(result);
            int rescode = joResult.getInt("rescode");
            if (rescode == 0) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
