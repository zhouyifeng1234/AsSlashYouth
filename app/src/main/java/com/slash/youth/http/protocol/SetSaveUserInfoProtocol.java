package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/8.
 */
public class SetSaveUserInfoProtocol extends BaseProtocol<SetBean> {
    private String name;
    private int careertype;
    private String desc;

    public SetSaveUserInfoProtocol(String name, int careertype, String desc) {
        this.name = name;
        this.careertype = careertype;
        this.desc = desc;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SAVE_USERINFO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("name",name);
        params.addBodyParameter("careertype", String.valueOf(careertype));
        params.addBodyParameter("desc",desc);
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
