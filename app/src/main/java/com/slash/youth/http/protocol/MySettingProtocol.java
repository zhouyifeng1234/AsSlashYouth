package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.MyAccountBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * Created by zss on 2016/11/8.
 */
public class MySettingProtocol extends BaseProtocol<SetBean> {
   /* private String  starttime;
    private String  endtime;
    private int status;

    public MySettingProtocol(String starttime, String endtime, int status) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.status = status;
    }*/


    private String url;
    private Map<String,String> map;

    public MySettingProtocol(String url, Map<String,String> map) {
        this.url = url;
        this.map = map;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVER_HOST_SETTING+url;
    }

    @Override
    public void addRequestParams(RequestParams params) {
       /* params.addBodyParameter("starttime", starttime);
        params.addBodyParameter("endtime", endtime);
        params.addBodyParameter("status", String.valueOf(status));*/
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            params.addBodyParameter(key, value);
        }

        map.clear();
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
