package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityLoginModel;
import com.slash.youth.utils.CommonUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class LoginActivity extends BaseActivity {

    public QQLoginUiListener qqLoginUiListener;
    public AuthInfo mAuthInfo;
    public SsoHandler mSsoHandler;
    public static Activity activity;
    private ActivityLoginBinding activityLoginBinding;
    private Long aLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        qqLoginUiListener = new QQLoginUiListener();

        mAuthInfo = new AuthInfo(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WEIBO, "www.slashyouth.com", "");
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ActivityLoginModel activityLoginModel = new ActivityLoginModel(activityLoginBinding, qqLoginUiListener, this, mSsoHandler, this);
        activityLoginBinding.setActivityLoginModel(activityLoginModel);

    }

    //    应用调用Andriod_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中增加如下代码：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginUiListener);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public class QQLoginUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            final JSONObject joResultData = (JSONObject) o;
//            LogKit.v(joResultData.toString());
//            try {
//                final String openid = joResultData.getString("openid");
//                final String access_token = joResultData.getString("access_token");
//
//                String phone = "18915521461";
//                String pin = "123456";
//                String _3pToken = access_token + "&" + openid + "&" + GlobalConstants.LoginPlatformType.QQ;
//                String userInfo = " & & & & ";
//                LoginManager.loginPhoneNum(phone, pin, _3pToken, userInfo);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject joUserInfo = null;
//                        try {
////                            joUserInfo = LoginManager.mTencent.request("get_user_info", null, Constants.HTTP_GET);
//                            joUserInfo = LoginManager.mTencent.request("get_user_info", null, "GET");
//                            LogKit.v(joUserInfo.toString());
//
////                            QQToken qqToken = new QQToken(access_token);
////                            qqToken.setOpenId(openid);
//                            UserInfo userinfo = new UserInfo(LoginActivity.this, LoginManager.mTencent.getQQToken());
//                            userinfo.getUserInfo(new IUiListener() {
//                                @Override
//                                public void onComplete(Object o) {
//                                    JSONObject jj = (JSONObject) o;
//                                    LogKit.v(jj.toString());
//                                }
//
//                                @Override
//                                public void onError(UiError uiError) {
//
//                                }
//
//                                @Override
//                                public void onCancel() {
//
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//            } catch (JSONException ex) {
//
//            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
