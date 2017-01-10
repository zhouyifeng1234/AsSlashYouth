package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/1/9.
 */
public class DelConversationListProtocol extends BaseProtocol<CommonResultBean> {

    private ArrayList<Long> conversationUidList;

    public DelConversationListProtocol(ArrayList<Long> conversationUidList) {
        this.conversationUidList = conversationUidList;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEL_CONVERSATION_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        try {
            JSONObject jo = new JSONObject();
            JSONArray jaUid = new JSONArray();
            for (int i = 0; i < conversationUidList.size(); i++) {
                long uid = conversationUidList.get(i);
                jaUid.put(uid);
            }
            jo.put("list", jaUid);
            LogKit.v(jo.toString());
            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
