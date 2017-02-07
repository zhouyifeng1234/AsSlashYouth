package com.slash.youth.http.protocol;

import android.content.Context;

import com.google.gson.Gson;
import com.slash.youth.domain.ResultErrorBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.utils.AuthHeaderUtils;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

abstract public class BaseProtocol<T> {

    public T dataBean;

    public ResultErrorBean resultErrorBean;

    private static SSLContext mSSLContext = null;

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

        //获取ssl,zss
        SSLContext sslContext = getSSLContext(CommonUtils.getApplication());

        if (null == sslContext) {
            if (BuildConfig.DEBUG)
                LogKit.d("Error:Can't Get SSLContext!");
            return null;
        }

        params.setSslSocketFactory(sslContext.getSocketFactory());
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


    /**
     * 获取Https的证书,zss
     *
     * @param context 上下文
     * @return SSL的上下文对象
     */
    public static SSLContext getSSLContext(Context context) {
        CertificateFactory certificateFactory = null;
        InputStream inputStream = null;
        Certificate cer = null;
        KeyStore keystore = null;
        TrustManagerFactory trustManagerFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            inputStream = context.getAssets().open("213980825410312.pem");//这里导入SSL证书文件

            cer = certificateFactory.generateCertificate(inputStream);
            // LogManager.i(TAG, cer.getPublicKey().toString());

            //创建一个证书库，并将证书导入证书库
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null); //双向验证时使用
            keystore.setCertificateEntry("trust", cer);

            // 实例化信任库
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            //信任所有证书 （官方不推荐使用）
//         s_sSLContext.init(null, new TrustManager[]{new X509TrustManager() {
//
//              @Override
//              public X509Certificate[] getAcceptedIssuers() {
//                  return null;
//              }
//
//              @Override
//              public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//
//              @Override
//              public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//          }}, new SecureRandom());

            return mSSLContext;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                IOUtils.close(inputStream);
                inputStream = null;
            }
        }
        return null;
    }
}
