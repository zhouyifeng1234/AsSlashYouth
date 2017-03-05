package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ThirdPartyLoginResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/13.
 */
public class ThirdPartyLoginProtocol extends BaseProtocol<ThirdPartyLoginResultBean> {

    private String _3pToken;
    private String _3pUid;
    private String loginPlatform;
    private String wechatOpenid;

    public ThirdPartyLoginProtocol(String _3pToken, String _3pUid, String loginPlatform, String wechatOpenid) {
        this._3pToken = _3pToken;
        this._3pUid = _3pUid;
        this.loginPlatform = loginPlatform;
        this.wechatOpenid = wechatOpenid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.THIRD_PARTY_LOGIN;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("3pToken", _3pToken);
        params.addBodyParameter("3pUid", _3pUid);
        params.addBodyParameter("loginPlatform", loginPlatform);
        if (wechatOpenid != null) {
            params.addBodyParameter("wechatOpenid", wechatOpenid);
        }
    }

    @Override
    public ThirdPartyLoginResultBean parseData(String result) {
        Gson gson = new Gson();
        ThirdPartyLoginResultBean thirdPartyLoginResultBean = gson.fromJson(result, ThirdPartyLoginResultBean.class);
        return thirdPartyLoginResultBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        //rescode状态较多，不适合在这里判断
        return true;
    }
}
