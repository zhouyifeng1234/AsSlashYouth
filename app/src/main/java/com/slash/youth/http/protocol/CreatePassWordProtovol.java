package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.MD5Utils;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/13.
 */
public class CreatePassWordProtovol extends BaseProtocol<SetBean> {
    private String pass;

    public CreatePassWordProtovol(String pass) {
        this.pass = pass;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CREATE_PASSWORD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        String createPass = MD5Utils.md5(pass);
        params.addBodyParameter("pass",createPass);
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
