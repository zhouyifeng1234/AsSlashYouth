package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.json.JSONArray;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zss on 2016/11/8.
 */
public class SetJsonArrayBaseProtocol extends BaseProtocol<SetBean> {

    private String url;
    private Map<String,String[]> jsonarrayMap;
    private  StringBuffer sb = new StringBuffer();

    public SetJsonArrayBaseProtocol(String url, Map<String,String[]>  jsonarrayMap) {
        this.url = url;
        this.jsonarrayMap = jsonarrayMap;

    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVER_HOST_MY_USERINFO +url;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        for (Map.Entry<String, String[]> stringStringEntry : jsonarrayMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String[] values = stringStringEntry.getValue();
            sb.append("[");
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);

            }

            sb.append("]");
            params.addBodyParameter(key,sb.toString());




        }
        jsonarrayMap.clear();
    }

    @Override
    public SetBean parseData(String result) {

        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);


        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }



}
