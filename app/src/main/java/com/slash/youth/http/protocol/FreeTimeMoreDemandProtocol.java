package com.slash.youth.http.protocol;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/8.
 */
public class FreeTimeMoreDemandProtocol extends BaseProtocol<FreeTimeMoreDemandBean>{
    private int pattern;
    private int isauth;
    private String city;
    private int sort;
    private double lng;
    private double lat;
    private int offset;
    private int limit;

    public FreeTimeMoreDemandProtocol(int pattern, int isauth, String city, int sort, double lng, double lat, int offset, int limit) {
        this.pattern = pattern;
        this.isauth = isauth;
        this.city = city;
        this.sort = sort;
        this.lng = lng;
        this.lat = lat;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MORE_DEMAND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if(pattern ==0||pattern == 1){
            params.addBodyParameter("pattern", String.valueOf(pattern));
        }

        if(isauth==0||isauth==1){
            params.addBodyParameter("isauth", String.valueOf(isauth));
        }

        if (city!=null) {
            if(!city.equals("全国")){
                params.addBodyParameter("city",city);
            }
        }

        if (sort==1||sort==2||sort==3||sort== 4) {
            params.addBodyParameter("sort", String.valueOf(4));
        }

       if(lng<180&&lng>-180){
            params.addBodyParameter("lng", String.valueOf(lng));
        }
        if(lat>-99&&lat<90){
            params.addBodyParameter("lat", String.valueOf(lat));
        }

        if (offset>0||offset==0) {
            params.addBodyParameter("offset", String.valueOf(offset));
        }
        if (limit>0&&limit<=20) {
            params.addBodyParameter("limit", String.valueOf(limit));
        }
    }

    @Override
    public FreeTimeMoreDemandBean parseData(String result) {
        Gson gson = new Gson();
        FreeTimeMoreDemandBean freeTimeDemandBean = gson.fromJson(result, FreeTimeMoreDemandBean.class);
        return freeTimeDemandBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
