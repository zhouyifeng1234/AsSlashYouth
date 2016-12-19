package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PublishServiceResultBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/22.
 */
public class SaveListTagProtocol extends BaseProtocol<SetBean> {

    private final ArrayList<String> listTag;


    public SaveListTagProtocol(ArrayList<String> listTag) {
        this.listTag = listTag;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_SLASH_TAG;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        try {
            JSONObject jo = new JSONObject();
            JSONArray jaTag = new JSONArray();
            for (String tag : listTag) {
                jaTag.put(tag);
            }
            jo.put("tag", jaTag);
            LogKit.v(jo.toString());
            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    PublishServiceResultBean mPublishServiceResultBean;

    @Override
    public boolean checkJsonResult(String result) {
       return true;
    }
}
