package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/2.
 */
public class SkillManagerProtocol extends BaseProtocol<SkillManagerBean> {
    private int offset;
    private int limit;

    public SkillManagerProtocol(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SKILL_MANAGE_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public SkillManagerBean parseData(String result) {
        Gson gson = new Gson();
        SkillManagerBean skillManagerBean = gson.fromJson(result, SkillManagerBean.class);
        return skillManagerBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
