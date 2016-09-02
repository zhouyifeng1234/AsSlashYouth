package com.slash.youth.http.protocol;

import com.slash.youth.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
        return params;
    }

    public String getUrlString() {
        return "http://www.baidu.com";
    }
}
