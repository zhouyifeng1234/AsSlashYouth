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
 * Created by zhouyifeng on 2017/1/19.
 */
public class DemandUpdateBidProtocol extends BaseProtocol<CommonResultBean> {

    private String id;
    private String quote;
    private ArrayList<Double> instalment;
    private String bp;
    private String starttime;

    public DemandUpdateBidProtocol(String id, String quote, ArrayList<Double> instalment, String bp, String starttime) {
        this.id = id;
        this.quote = quote;
        this.instalment = instalment;
        this.bp = bp;
        this.starttime = starttime;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_UPDATE_BID;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        try {
            JSONObject jo = new JSONObject();
            jo.put("id", id);
            jo.put("quote", quote);
            jo.put("starttime", starttime);
            JSONArray jaInstalmentRatio = new JSONArray();
            for (double ratio : instalment) {
                jaInstalmentRatio.put(ratio / 100.0d);
            }
            jo.put("instalment", jaInstalmentRatio);
            jo.put("bp", bp);

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
