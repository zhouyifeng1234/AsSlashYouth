package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 登录后 完善技能标签,设置用户的技能标签（三级标签）
 * <p/>
 * Created by zhouyifeng on 2016/12/15.
 */
public class LoginSetTagProtocol extends BaseProtocol<CommonResultBean> {
    private ArrayList<String> listTag;

    public LoginSetTagProtocol(ArrayList<String> listTag) {
        this.listTag = listTag;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGIN_SET_TAG;
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
            LogKit.v("------------------设置技能标签:" + jo.toString());
            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
