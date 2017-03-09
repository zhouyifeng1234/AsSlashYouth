package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.DemandPurposeListBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/2.
 */
public class DeteleSkillManagerItemProtocol extends BaseProtocol<SetBean> {
    private long id;

    public DeteleSkillManagerItemProtocol(long id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SKILL_MANAGE_DELETE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", String.valueOf(id));

    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
