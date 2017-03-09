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
 * Created by zhouyifeng on 2016/12/8.
 */
public class ServiceFlowSelectedProtocol extends BaseProtocol<CommonResultBean> {

    private String soid;
    private String uid;
    private String quote;
    private String starttime;
    private String endtime;
    private ArrayList<Double> instalment;
    private String bp;
    private String ismodify;

    public ServiceFlowSelectedProtocol(String soid, String uid, String quote, String starttime, String endtime, ArrayList<Double> instalment, String bp, String ismodify) {
        this.soid = soid;
        this.uid = uid;
        this.quote = quote;
        this.starttime = starttime;
        this.endtime = endtime;
        this.instalment = instalment;
        this.bp = bp;
        this.ismodify = ismodify;
    }


    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SERVICE_FLOW_SELECTED;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        try {
            JSONObject jo = new JSONObject();
            jo.put("soid", soid);
            jo.put("uid", uid);
            jo.put("quote", quote);
            jo.put("starttime", starttime);
            jo.put("endtime", endtime);
            JSONArray jaInstalmentRatio = new JSONArray();
            for (double ratio : instalment) {
                jaInstalmentRatio.put(ratio / 100.0d);
            }
            jo.put("instalment", jaInstalmentRatio);
            jo.put("bp", bp);
            jo.put("ismodify", ismodify);

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
        if (commonResultBean.rescode == 0) {
            if (commonResultBean.data.status == 1) {
                return true;
            }
        }
        return false;
    }
}
