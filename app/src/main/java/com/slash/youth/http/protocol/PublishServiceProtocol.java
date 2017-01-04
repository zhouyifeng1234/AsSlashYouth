package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PublishServiceResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/22.
 */
public class PublishServiceProtocol extends BaseProtocol<PublishServiceResultBean> {

    private final String title;
    private final ArrayList<String> listTag;
    private final long startime;
    private final long endtime;
    private final int anonymity;
    private final String desc;
    private final int timetype;
    private final ArrayList<String> listPic;
    private final int instalment;
    private final int bp;
    private final int pattern;
    private final String place;
    private final double lng;
    private final double lat;
    private final double quote;
    private final int quoteunit;

    public PublishServiceProtocol(String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, int timetype, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        this.title = title;
        this.listTag = listTag;
        this.startime = startime;
        this.endtime = endtime;
        this.anonymity = anonymity;
        this.desc = desc;
        this.timetype = timetype;
        this.listPic = listPic;
        this.instalment = instalment;
        this.bp = bp;
        this.pattern = pattern;
        this.place = place;
        this.lng = lng;
        this.lat = lat;
        this.quote = quote;
        this.quoteunit = quoteunit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.PUBLISH_SERVICE;
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
            jo.put("starttime", startime);
            jo.put("endtime", endtime);
            jo.put("anonymity", anonymity);
            jo.put("desc", desc);
            jo.put("timetype", timetype);
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
            jo.put("lng", lng);
            jo.put("lat", lat);
            jo.put("quote", quote);
            jo.put("quoteunit", quoteunit);

            LogKit.v(jo.toString());
            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PublishServiceResultBean parseData(String result) {
        return mPublishServiceResultBean;
    }

    PublishServiceResultBean mPublishServiceResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        mPublishServiceResultBean = gson.fromJson(result, PublishServiceResultBean.class);
        if (mPublishServiceResultBean.rescode == 0 && mPublishServiceResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
