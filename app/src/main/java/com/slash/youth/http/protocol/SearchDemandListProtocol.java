package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

import java.util.Observable;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchDemandListProtocol extends BaseProtocol<SearchItemDemandBean>{
    private String tag;
    private int pattern;
    private int isauth;
    private  String city;
    private int sort;
    private double lat;
    private double lng;
    private int offset;
    private int limit;

    public SearchDemandListProtocol(String tag, int pattern, int isauth, String city, int sort, double lat, double lng, int offset, int limit) {
        this.tag = tag;
        this.pattern = pattern;
        this.isauth = isauth;
        this.city = city;
        this.sort = sort;
        this.lat = lat;
        this.lng = lng;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_DEMAND;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tag",tag);
        if(pattern ==0||pattern == 1){
            params.addBodyParameter("pattern", String.valueOf(pattern));
        }
        if(isauth ==1||isauth ==0){
            params.addBodyParameter("isauth", String.valueOf(isauth));
        }
        if(city!=null){
            if(!city.equals("全国")){
                params.addBodyParameter("city",city);
            }
        }
        if(sort ==1||sort==2||sort==3||sort == 4){
            params.addBodyParameter("sort", String.valueOf(sort));
        }
        if(lng<180&&lng>-180){
            params.addBodyParameter("lng", String.valueOf(lng));
        }
        if(lat>-99&&lat<90){
            params.addBodyParameter("lat", String.valueOf(lat));
        }
        if (offset>0&&offset==0) {
            params.addBodyParameter("offset", String.valueOf(offset));
        }
        if (limit>0&&limit<=20) {
            params.addBodyParameter("limit", String.valueOf(limit));
        }

    }

    @Override
    public SearchItemDemandBean parseData(String result) {
        Gson gson = new Gson();
        SearchItemDemandBean searchItemDemandBean = gson.fromJson(result, SearchItemDemandBean.class);
        return searchItemDemandBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
