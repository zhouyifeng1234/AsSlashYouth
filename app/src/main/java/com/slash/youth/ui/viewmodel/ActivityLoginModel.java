package com.slash.youth.ui.viewmodel;


import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.View;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.PerfectInfoActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class ActivityLoginModel extends BaseObservable {
    ActivityLoginBinding mActivityLoginBinding;
    LoginActivity.QQLoginUiListener qqLoginUiListener;
    LoginActivity loginActivity;
    SsoHandler mSsoHandler;


    public ActivityLoginModel(ActivityLoginBinding activityLoginBinding, LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity, SsoHandler ssoHandler) {
        this.mActivityLoginBinding = activityLoginBinding;
        this.qqLoginUiListener = qqLoginUiListener;
        this.loginActivity = loginActivity;
        this.mSsoHandler = ssoHandler;
    }

    /**
     * 登录按钮点击事件
     *
     * @param v
     */
    public void login(View v) {
        //TODO 具体的登录逻辑，等服务端相关接口完成以后再实现
        Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
        intentPerfectInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPerfectInfoActivity);

//        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentHomeActivity);

//        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
//        String pin = mActivityLoginBinding.etActivityLoginVerificationCode.getText().toString();
//        LoginManager.checkPhoneVerificationCode(phoenNum, pin);


    }

    public void sendPhoneVerificationCode(View v) {
        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
        LogKit.v(phoenNum);
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(phoenNum);

    }

    public void wechatLogin(View v) {
//        LoginManager.loginWeChat();


        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    public void qqLogin(View v) {
//        LoginManager.loginQQ(qqLoginUiListener, loginActivity);


        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.QQ, umAuthListener);
    }

    public void weiboLogin(View v) {
//        mSsoHandler.authorize(new SlashWeiboAuthListener());

        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.SINA, umAuthListener);

    }

    public class SlashWeiboAuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                String token = mAccessToken.getToken();
                String uid = mAccessToken.getUid();
                LogKit.v("weibo token:" + token + "    weibo uid:" + uid);
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code", "");

            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtils.shortToast("Authorize fail");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtils.shortToast("Authorize cancel");
        }
    };

}
