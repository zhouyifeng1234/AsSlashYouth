package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.TokenLoginResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/12.
 */
public class TokenLoginProtocol extends BaseProtocol<TokenLoginResultBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.TOKEN_LOGIN;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        //需要随便加个字符串，不然那服务端会报错
        params.addBodyParameter("a", "a");
    }

    @Override
    public TokenLoginResultBean parseData(String result) {
        return tokenLoginResultBean;
    }

    TokenLoginResultBean tokenLoginResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        tokenLoginResultBean = gson.fromJson(result, TokenLoginResultBean.class);
        if (tokenLoginResultBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
