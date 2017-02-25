package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyThridPartyBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ThirdPartyModel;
import com.slash.youth.utils.CommonUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by zss on 2016/11/4.
 */
public class BindThridPartyActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private FrameLayout fl;
    private ActivityMyThridPartyBinding activityMyThridPartyBinding;
    private String titleString = "第三方账号";
    public QQLoginUiListener qqLoginUiListener;
    public SsoHandler mSsoHandler;
    public AuthInfo mAuthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qqLoginUiListener = new QQLoginUiListener();
        mAuthInfo = new AuthInfo(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WEIBO, "www.slashyouth.com", "");
        mSsoHandler = new SsoHandler(BindThridPartyActivity.this, mAuthInfo);

        activityMyThridPartyBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_thrid_party);
        ThirdPartyModel thirdPartyModel = new ThirdPartyModel(activityMyThridPartyBinding,this);
        activityMyThridPartyBinding.setThirdPartyModel(thirdPartyModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
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
