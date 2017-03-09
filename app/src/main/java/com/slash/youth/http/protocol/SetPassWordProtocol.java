package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SetTimeBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.MD5Utils;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/13.
 */
public class SetPassWordProtocol extends BaseProtocol<SetBean> {
    private String oldpass;
    private String newpass;
    public SetPassWordProtocol(String oldpass, String newpass) {
        this.oldpass = oldpass;
        this.newpass = newpass;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_PASSWORD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        String md5OldPass = MD5Utils.md5(oldpass);
        String md5NewPass = MD5Utils.md5(newpass);
        params.addBodyParameter("oldpass",md5OldPass);
        params.addBodyParameter("newpass",md5NewPass);
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
