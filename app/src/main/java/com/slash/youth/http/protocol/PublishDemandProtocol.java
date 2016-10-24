package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;

/**
 * Created by zhouyifeng on 2016/9/29.
 */
public class PublishDemandProtocol extends BaseProtocol<String> {
    private String title;
    private String label;
    private String tasktime;
    private String fighttime;
    private String anonymity;
    private String desc;
    private String pic;
    private String type;
    private String proofbox;
    private String invoice;
    private String pattern;
    private String place;
    private String placedetail;
    private String consume;
    private String lng;
    private String lat;
    private String offer;
    private String quote;

    public PublishDemandProtocol(String title, String label, String tasktime, String fighttime, String anonymity, String desc, String pic, String type, String proofbox, String invoice, String pattern, String place, String placedetail, String consume, String lng, String lat, String offer, String quote) {
        this.title = title;
        this.label = label;
        this.tasktime = tasktime;
        this.fighttime = fighttime;
        this.anonymity = anonymity;
        this.desc = desc;
        this.pic = pic;
        this.type = type;
        this.proofbox = proofbox;
        this.invoice = invoice;
        this.pattern = pattern;
        this.place = place;
        this.placedetail = placedetail;
        this.consume = consume;
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


//        params.addBodyParameter("title", title);
//        params.addBodyParameter("label", label);
//        params.addBodyParameter("tasktime", tasktime);
//        params.addBodyParameter("fighttime", fighttime);
//        params.addBodyParameter("anonymity", anonymity);
//        params.addBodyParameter("desc", desc);
//        params.addBodyParameter("pic", pic);
//        params.addBodyParameter("type", type);
//        params.addBodyParameter("proofbox", proofbox);
//
//        params.addBodyParameter("invoice", invoice);
//        params.addBodyParameter("pattern", pattern);
//        params.addBodyParameter("place", place);
//        params.addBodyParameter("placedetail", placedetail);
//        params.addBodyParameter("consume", consume);
//        params.addBodyParameter("lng", lng);
//        params.addBodyParameter("lat", lat);
//        params.addBodyParameter("offer", offer);
//
//        params.addBodyParameter("quote", quote);


//        params.addBodyParameter("title", "算法提取");
//        params.addBodyParameter("tag", "['1-4-APP']");
//        params.addBodyParameter("starttime", System.currentTimeMillis() + 48 * 60 * 60 * 1000 + "");
//        params.addBodyParameter("anonymity", 1 + "");
//        params.addBodyParameter("desc", "研发");
//        params.addBodyParameter("pic", "['http://img05.tooopen.com/images/20140919/sy_71272488121.jpg']");
//        params.addBodyParameter("instalment", 1 + "");
//
////        params.addBodyParameter("invoice", 1 + "");
//        params.addBodyParameter("pattern", 1 + "");
//        params.addBodyParameter("place", "苏州");
//        params.addBodyParameter("placedetail", "苏州工业园区");
////        params.addBodyParameter("consume", 1 + "");
//        params.addBodyParameter("lng", 30.000001D + "");
//        params.addBodyParameter("lat", 30.000001D + "");
//        params.addBodyParameter("offer", 0 + "");
//
//        params.addBodyParameter("quote", 100.00D + "");


//        params.addBodyParameter("title", "微信扫一扫评论简单易赚在线审核100");
//        params.addBodyParameter("tag", "[\"1-5-画册设计\",\"1-4-宣传品设计\",\"1-3-重复标签\",\"1-3-重复标签\"]");
//        params.addBodyParameter("starttime", System.currentTimeMillis() + 10000000 + "");
//        params.addBodyParameter("anonymity", 1 + "");
//        params.addBodyParameter("desc", "共需要 200个稿件");
//
//        params.addBodyParameter("instalment", "1");
//        params.addBodyParameter("pattern", "1");
//        params.addBodyParameter("offer", "0");
//        params.addBodyParameter("quote", "100.00");
//        params.addBodyParameter("pic", "[\"http://img05.tooopen.com/images/20140919/sy_71272488121.jpg\", \"http://img06.tooopen.com/images/20161012/tooopen_sy_181713275376.jpg\"]");
//
//
//        params.addBodyParameter("place", "北京朝阳区望京soho");
//        params.addBodyParameter("placedetail", "soho塔3-34楼");
//        params.addBodyParameter("lng", "39.9936252828");
//        params.addBodyParameter("lat", "116.4736562349");

        params.addBodyParameter("title", "微信扫一扫评论简单易赚在线审核100");
        params.addBodyParameter("tag", "['1-5-画册设计']");
        params.addBodyParameter("anonymity", "1");
        params.addBodyParameter("pattern", "1");

        try {
            JSONObject jo = new JSONObject();
            jo.put("title", "微信扫一扫评论简单易赚在线审核100");
            JSONArray ja = new JSONArray();
            ja.put("1-5-画册设计");
            ja.put("1-2-APP");
            jo.put("tag", ja);
            jo.put("anonymity", 1);
            jo.put("pattern", 1);

            StringBody stringBody = new StringBody(jo.toString(), null);
            params.setRequestBody(stringBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String parseData(String result) {

        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
