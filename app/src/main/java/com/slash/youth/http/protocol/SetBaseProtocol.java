package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * Created by zss on 2016/11/8.
 */
public class SetBaseProtocol extends BaseProtocol<SetBean> {

    private String url;
    private Map<String,String> stringMap;

    public SetBaseProtocol(String url, Map<String,String> stringMap) {
        this.url = url;
        this.stringMap = stringMap;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVER_HOST_MY_USERINFO +url;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        for (Map.Entry<String, String> stringStringEntry : stringMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            params.addBodyParameter(key, value);
        }
        stringMap.clear();
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
