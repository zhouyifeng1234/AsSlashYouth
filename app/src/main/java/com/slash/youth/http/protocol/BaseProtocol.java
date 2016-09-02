package com.slash.youth.http.protocol;

import com.slash.youth.utils.AuthHeaderUtils;
import com.slash.youth.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

public class BaseProtocol {

    public void getDataFromServer() {
        x.http().post(getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogUtils.v("onSuccess");
                LogUtils.v(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.v("onError");
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.v("onCancelled");
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogUtils.v("onFinished");
            }
        });
    }


    public RequestParams getRequestParams() {
        RequestParams params = new RequestParams(getUrlString());

        Map headerMap = AuthHeaderUtils.getBasicAuthHeader("POST", getUrlString());
        String date = (String) headerMap.get("Date");
        String authorizationStr = (String) headerMap.get("Authorization");
        params.addHeader("Authorization", authorizationStr);
        params.addHeader("Date", date);

        //[用户信息]-获取用户技能标签
        params.addBodyParameter("uid", "20");

        //第三方登录
//        params.addBodyParameter("code", "20");
//        params.addBodyParameter("loginPlatform", "3");


        //融云token
//        params.addBodyParameter("uid", "20");
//        params.addBodyParameter("phone", "13353471234");


        params.setAsJsonContent(true);
        return params;
    }

    public String getUrlString() {
        //[用户信息]-获取用户技能标签
        return "http://121.42.145.178/uinfo/v1/api/vcard/skilllabel/get";


        //第三方登录
//        return "http://121.42.145.178/auth/login/thirdParty";

        //获取融云token
        // return "http://121.42.145.178/auth/rongToken";
    }

}
