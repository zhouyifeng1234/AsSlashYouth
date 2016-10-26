package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.domain.SkillLabelAllBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/10/25.
 */
public class SkillLabelGetProtocol extends BaseProtocol<SkillLabelGetBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SKILLLABEL_GET;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("{}","{}");
    }

    @Override
    public SkillLabelGetBean parseData(String result) {
        Gson gson = new Gson();
        SkillLabelGetBean skillLabelGetBean = gson.fromJson(result, SkillLabelGetBean.class);
        return skillLabelGetBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
