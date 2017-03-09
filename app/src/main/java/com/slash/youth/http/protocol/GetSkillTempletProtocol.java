package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/3.
 */
public class GetSkillTempletProtocol extends BaseProtocol<SkillMamagerOneTempletBean> {
    private long id;

    public GetSkillTempletProtocol(long id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_SKILL_TEMPLET;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", String.valueOf(id));
    }

    @Override
    public SkillMamagerOneTempletBean parseData(String result) {
        Gson gson = new Gson();
        SkillMamagerOneTempletBean  skillMamagerOneTempletBean = gson.fromJson(result, SkillMamagerOneTempletBean.class);
        return skillMamagerOneTempletBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
