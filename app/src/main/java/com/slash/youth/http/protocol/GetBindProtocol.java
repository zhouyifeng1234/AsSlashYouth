package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.GetBindBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by acer on 2016/11/13.
 */
public class GetBindProtocol extends BaseProtocol<GetBindBean> {
    private String token;

    public GetBindProtocol(String token) {
        this.token = token;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_BINDING;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("token",token);
    }

    @Override
    public GetBindBean parseData(String result) {
        Gson gson = new Gson();
        GetBindBean getBindBean = gson.fromJson(result, GetBindBean.class);
        return getBindBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
