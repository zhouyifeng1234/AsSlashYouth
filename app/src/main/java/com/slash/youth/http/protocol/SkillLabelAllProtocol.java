package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.domain.SkillLabelAllBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zss on 2016/10/24.
 */
public class SkillLabelAllProtocol extends BaseProtocol< ArrayList<SkillLabelAllBean>>  {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SKILLLABEL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
    }

    @Override
    public  ArrayList<SkillLabelAllBean> parseData(String result) {

        Gson gson = new Gson();
       // ArrayList<SkillLabelAllBean> skillLabelAllBean = gson.fromJson(result, ArrayList.class);
        Type listType = new TypeToken<ArrayList<SkillLabelAllBean>>(){}.getType();
        ArrayList<SkillLabelAllBean> skillLabelAllBeans = gson.fromJson(result, listType);

        return skillLabelAllBeans;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
