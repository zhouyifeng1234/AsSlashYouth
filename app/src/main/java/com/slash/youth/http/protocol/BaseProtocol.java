package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ResultErrorBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.utils.AuthHeaderUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

abstract public class BaseProtocol<T> {

    public T dataBean;

    public ResultErrorBean resultErrorBean;

    public interface IResultExecutor<T> {
        void execute(T dataBean);

        //        void executeError(ResultErrorBean resultErrorBean);
        void executeResultError(String result);
    }

    public void getDataFromServer(final IResultExecutor resultExecutor) {
        x.http().post(getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogKit.v("onSuccess");
                LogKit.v(result);
                boolean isResultSuccess = checkJsonResult(result);
                if (isResultSuccess) {
                    dataBean = parseData(result);
                    resultExecutor.execute(dataBean);
                } else {
                    resultExecutor.executeResultError(result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.shortToast("加载网络数据失败");
                LogKit.v("onError");
                ex.printStackTrace();
            }


            @Override
            public void onCancelled(CancelledException cex) {
                LogKit.v("onCancelled");
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogKit.v("onFinished");
            }
        });
    }


    public RequestParams getRequestParams() {
        RequestParams params = new RequestParams(getUrlString());
        params.setAsJsonContent(true);
        addRequestHeader(params);

        addRequestParams(params);

        //[用户信息]-获取用户技能标签
//        params.addBodyParameter("uid", "20");

        //第三方登录
//        params.addBodyParameter("code", "20");
//        params.addBodyParameter("loginPlatform", "3");


        //融云token
//        params.addBodyParameter("uid", "20");
//        params.addBodyParameter("phone", "13353471234");

        return params;
    }


    public void addRequestHeader(RequestParams params) {
        Map headerMap = AuthHeaderUtils.getBasicAuthHeader("POST", getUrlString());
        String date = (String) headerMap.get("Date");
        String authorizationStr = (String) headerMap.get("Authorization");
        LogKit.v("date:" + date);
        LogKit.v("authorizationStr:" + authorizationStr);
        params.addHeader("Date", date);
        params.addHeader("Authorization", authorizationStr);
        //跳过验证
        params.addHeader("uid", LoginManager.currentLoginUserId + "");
        params.addHeader("pass", "1");
        params.addHeader("token", LoginManager.token);
        params.addHeader("Content-Type", "application/json");
    }

    public ResultErrorBean parseErrorResultData(String result) {
        Gson gson = new Gson();
        ResultErrorBean resultErrorBean = gson.fromJson(result, ResultErrorBean.class);
        return resultErrorBean;
    }

    public abstract String getUrlString();

    public abstract void addRequestParams(RequestParams params);

    public abstract T parseData(String result);

    public abstract boolean checkJsonResult(String result);

    //[用户信息]-获取用户技能标签
//    return "http://121.42.145.178/uinfo/v1/api/vcard/skilllabel/get";


    //第三方登录
//        return "http://121.42.145.178/auth/login/thirdParty";

    //获取融云token
    // return "http://121.42.145.178/auth/rongToken";
































   /* public T dataBean;

    public ResultErrorBean resultErrorBean;

    public interface IResultExecutor<T> {
        void execute(T dataBean);

        //        void executeError(ResultErrorBean resultErrorBean);
        void executeResultError(String result);
    }

    public void getDataFromServer(final IResultExecutor resultExecutor) {
        x.http().post(getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogKit.v("onSuccess");
                LogKit.v(result);
                boolean isResultSuccess = checkJsonResult(result);
                if (isResultSuccess) {
                    dataBean = parseData(result);
                    resultExecutor.execute(dataBean);
                } else {
                    resultExecutor.executeResultError(result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogKit.v("onError");
                ex.printStackTrace();
            }


            @Override
            public void onCancelled(CancelledException cex) {
                LogKit.v("onCancelled");
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogKit.v("onFinished");
            }
        });
    }


    public RequestParams getRequestParams() {
        RequestParams params = new RequestParams(getUrlString());
        params.setAsJsonContent(true);
        addRequestHeader(params);

        addRequestParams(params);

        //[用户信息]-获取用户技能标签
//        params.addBodyParameter("uid", "20");

        //第三方登录
//        params.addBodyParameter("code", "20");
//        params.addBodyParameter("loginPlatform", "3");


        //融云token
//        params.addBodyParameter("uid", "20");
//        params.addBodyParameter("phone", "13353471234");

        return params;
    }


    public void addRequestHeader(RequestParams params) {

        Map headerMap = AuthHeaderUtils.getBasicAuthHeader("POST", getUrlString());
        String date = (String) headerMap.get("Date");
        String authorizationStr = (String) headerMap.get("Authorization");
        LogKit.d("date:" + date);
        LogKit.d("authorizationStr:" + authorizationStr);
        params.addHeader("Date", date);
        params.addHeader("Authorization", authorizationStr);
        //跳过验证
        params.addHeader("uid", LoginManager.currentLoginUserId + "");
        params.addHeader("pass", "1");
        params.addHeader("Content-Type", "application/json");
    }

    public ResultErrorBean parseErrorResultData(String result) {

        Gson gson = new Gson();
        ResultErrorBean resultErrorBean = gson.fromJson(result, ResultErrorBean.class);
        return resultErrorBean;
    }

    public abstract String getUrlString();

    public abstract void addRequestParams(RequestParams params);

    public abstract T parseData(String result);

    public abstract boolean checkJsonResult(String result);

    //[用户信息]-获取用户技能标签
//    return "http://121.42.145.178/uinfo/v1/api/vcard/skilllabel/get";


    //第三方登录
//        return "http://121.42.145.178/auth/login/thirdParty";

    //获取融云token
    // return "http://121.42.145.178/auth/rongToken";*/

}
