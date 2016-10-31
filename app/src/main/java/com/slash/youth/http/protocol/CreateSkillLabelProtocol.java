package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/25.
 */
public class CreateSkillLabelProtocol extends BaseProtocol<SkillLabelGetBean> {
    private int f1;
    private int f2;
    private String tags;

    public CreateSkillLabelProtocol(int f1, int f2, String tags) {
        this.f1 = f1;
        this.f2 = f2;
        this.tags = tags;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SKILLLABEL_CREATE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("f1",f1+"");
        params.addBodyParameter("f2",f2+"");
        params.addBodyParameter("tags", tags);
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
