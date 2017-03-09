package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PublishDemandResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/29.
 */
public class PublishDemandProtocol extends BaseProtocol<PublishDemandResultBean> {
    private String title;
    private ArrayList<String> listTag;
    private String starttime;
    private String anonymity;
    private String desc;

    private ArrayList<String> listPic;
    private String instalment;
    private String bp;
    private String pattern;
    private String place;

    private String placedetail;
    private String lng;
    private String lat;
    private String offer;
    private String quote;

    public PublishDemandProtocol(String title, ArrayList<String> listTag, String starttime, String anonymity, String desc, ArrayList<String> listPic, String instalment, String bp, String pattern, String place, String placedetail, String lng, String lat, String offer, String quote) {
        this.title = title;
        this.listTag = listTag;
        this.starttime = starttime;
        this.anonymity = anonymity;
        this.desc = desc;

        this.listPic = listPic;
        this.instalment = instalment;
        this.bp = bp;
        this.pattern = pattern;
        this.place = place;

        this.placedetail = placedetail;
        this.lng = lng;
        this.lat = lat;
        this.offer = offer;
        this.quote = quote;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.PUBLISH_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {

        try {
            JSONObject jo = new JSONObject();
            jo.put("title", title);
            JSONArray jaTag = new JSONArray();
            for (String tag : listTag) {
                jaTag.put(tag);
            }
            jo.put("tag", jaTag);
            if (!starttime.equals("-1")) {
                jo.put("starttime", starttime);
            }
            jo.put("anonymity", anonymity);
            jo.put("desc", desc);
            if (listPic.size() > 0) {
                JSONArray jaPic = new JSONArray();
                for (String picUrl : listPic) {
                    jaPic.put(picUrl);
                }
                jo.put("pic", jaPic);
            }
            jo.put("instalment", instalment);
            jo.put("bp", bp);
            jo.put("pattern", pattern);
            jo.put("place", place);
            jo.put("placedetail", placedetail);
            jo.put("lng", lng);
            jo.put("lat", lat);
            jo.put("offer", offer);
            jo.put("quote", quote);

            LogKit.v(jo.toString());
            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PublishDemandResultBean parseData(String result) {
        return publishDemandResultBean;
    }

    PublishDemandResultBean publishDemandResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        publishDemandResultBean = gson.fromJson(result, PublishDemandResultBean.class);
        if (publishDemandResultBean.rescode == 0 && publishDemandResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
