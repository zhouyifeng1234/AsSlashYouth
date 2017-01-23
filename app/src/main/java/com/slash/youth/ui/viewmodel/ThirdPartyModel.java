package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.databinding.BaseObservable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.databinding.ActivityMyThridPartyBinding;
import com.slash.youth.domain.GetBindBean;
import com.slash.youth.domain.LoginBindBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.GetBindProtocol;
import com.slash.youth.http.protocol.LoginBindProtocol;
import com.slash.youth.http.protocol.LoginUnBindProtocol;
import com.slash.youth.ui.activity.BindThridPartyActivity;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * Created by zss on 2016/11/4.
 */
public class ThirdPartyModel extends BaseObservable {
    private ActivityMyThridPartyBinding activityMyThridPartyBinding;
    private boolean isWinxinBing = false;
    private boolean isQQBing = false;
    private boolean isWinboBing = false;
    private BindThridPartyActivity bindThridPartyActivity;
    private final LoginManager loginManager;
    private int type;

    public ThirdPartyModel(ActivityMyThridPartyBinding activityMyThridPartyBinding,BindThridPartyActivity bindThridPartyActivity) {
        this.activityMyThridPartyBinding = activityMyThridPartyBinding;
        this.bindThridPartyActivity = bindThridPartyActivity;
        loginManager = new LoginManager();
        initData();
    }

    private void initData() {
        bindPlatform(LoginManager.token);
    }

    private void initView(boolean isWinxinBing,boolean isQQBing,boolean isWinboBing){
        if(isWinxinBing){
            activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
        }else {
            activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
        }

        if(isQQBing){
            activityMyThridPartyBinding.tvQQBinding.setText("解绑");
        }else {
            activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
        }
    }

    //绑定平台
    public void bindPlatform(String token) {
        GetBindProtocol getBindProtocol = new GetBindProtocol(token);
        getBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<GetBindBean>() {
            @Override
            public void execute(GetBindBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    GetBindBean.DataBean data = dataBean.getData();
                    List<String> platforms = data.getPlatforms();
                    for (String platform : platforms) {
                        if(platform.equals("1")){
                            isWinxinBing = true;
                            type = 1;
                        }else if(platform.equals("2")){
                            isQQBing = true;
                            type = 2;
                        }else if(platform.equals("3")){
                            isWinboBing = true;
                            type = 3;
                        }
                    }
                    initView(isWinxinBing,isQQBing,isWinboBing);
                }
            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

    //微信
    public void weixin(View view){
        UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
        mShareAPI.doOauthVerify(bindThridPartyActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    //qq
    public void qq(View view){
        UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
        mShareAPI.doOauthVerify(bindThridPartyActivity, SHARE_MEDIA.QQ, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
            switch (platform) {
                case QQ:
                    String QQ_access_token = data.get("access_token");
                    String uid = data.get("uid");



                    if(isQQBing){
                        LogKit.d("==jie==");
                        loginUnBind(GlobalConstants.LoginPlatformType.QQ);
                    }else {
                        LogKit.d("===bind==");
                        loginBind(QQ_access_token,uid, GlobalConstants.LoginPlatformType.QQ);
                    }

                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String openid = data.get("unionid");

                    if(isWinxinBing){
                        loginUnBind(GlobalConstants.LoginPlatformType.WECHAT);

                    }else {
                        loginBind(WEIXIN_access_token,openid, GlobalConstants.LoginPlatformType.WECHAT);
                    }
                    break;
            }
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

    //绑定第三方账号
    public void loginBind(String token,String uid,Byte loginPlatform) {
        LoginBindProtocol loginBindProtocol = new LoginBindProtocol(token,uid,loginPlatform);
        loginBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<LoginBindBean>() {
            @Override
            public void execute(LoginBindBean dataBean) {
                LoginBindBean.DataBean data = dataBean.getData();
                String token1 = data.getToken();
                String uid1 = data.getUid();
                LogKit.d("bind返回值:"+token1+" "+uid1);

                switch (type){
                    case 1://微信
                        activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
                        break;
                    case 2://qq
                        activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
                        break;
                }
            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

    //解绑第三方账号
    public void loginUnBind(Byte loginPlatform) {
        LoginUnBindProtocol loginUnBindProtocol = new LoginUnBindProtocol(loginPlatform);
        loginUnBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<LoginBindBean>() {
            @Override
            public void execute(LoginBindBean dataBean) {
                LoginBindBean.DataBean data = dataBean.getData();
                String token2 = data.getToken();
                String uid2 = data.getUid();
                LogKit.d("unbind返回值:"+token2+" "+uid2);
                switch (type){
                    case 1://微信
                        activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
                        break;
                    case 2://qq
                        activityMyThridPartyBinding.tvQQBinding.setText("解绑");
                        break;
                }

            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

    //微博
    public void weibo(View view){
       /* if(isWinboBing){
            activityMyThridPartyBinding.tvWeiboBinding.setText("去绑定");
           // LoginManager.serverThirdPartyLogin();
           loginBind(" token的值 ",GlobalConstants.ThirdAppId.APPID_WEIBO, GlobalConstants.LoginPlatformType.WEIBO);
            SpUtils.setBoolean("isWinbo",false);
        }else {
            activityMyThridPartyBinding.tvWeiboBinding.setText("解绑");
            loginUnBind(GlobalConstants.LoginPlatformType.WEIBO);
            SpUtils.setBoolean("isWinbo",true);
        }*/
    }

}
