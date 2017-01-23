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
                            //type = GlobalConstants.LoginPlatformType.WECHAT;
                        }else if(platform.equals("2")){
                            isQQBing = true;
                            //type = GlobalConstants.LoginPlatformType.QQ;
                        }else if(platform.equals("3")){
                            isWinboBing = true;
                            //type = GlobalConstants.LoginPlatformType.WEIBO;
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
        type = GlobalConstants.LoginPlatformType.WECHAT;
        if(isWinxinBing){
            loginUnBind(GlobalConstants.LoginPlatformType.WECHAT);

        }else {
            UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
            mShareAPI.doOauthVerify(bindThridPartyActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(bindThridPartyActivity, mPermissionList, 123);
            }
        }
    }

    //qq
    public void qq(View view){
        type = GlobalConstants.LoginPlatformType.QQ;
        if(isQQBing){
            loginUnBind(GlobalConstants.LoginPlatformType.QQ);

        }else {
            UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
            if (mShareAPI.isInstall(bindThridPartyActivity, SHARE_MEDIA.QQ)) {
                mShareAPI.doOauthVerify(bindThridPartyActivity, SHARE_MEDIA.QQ, umAuthListener);
            } else {
                ToastUtils.shortToast("请先安装QQ客户端");
            }
        }
    }



    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
            UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
            switch (platform) {
                case QQ:
                    String  QQ_access_token = data.get("access_token");
                    String  QQ_uid = data.get("uid");
                    LogKit.d("==QQ_access_token="+QQ_access_token+"==qquid="+QQ_uid);
                    loginBind(QQ_access_token,QQ_uid, GlobalConstants.LoginPlatformType.QQ);
                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String openid = data.get("unionid");
                    LogKit.d("===WEIXIN_access_token=="+WEIXIN_access_token+"=openid="+openid);
                    loginBind(WEIXIN_access_token,openid, GlobalConstants.LoginPlatformType.WECHAT);
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
        final LoginBindProtocol loginBindProtocol = new LoginBindProtocol(token,uid,loginPlatform);
        loginBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<LoginBindBean>() {
            @Override
            public void execute(LoginBindBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    switch (type){
                        case GlobalConstants.LoginPlatformType.WECHAT://微信
                            activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
                            isWinxinBing = true;
                            break;
                        case GlobalConstants.LoginPlatformType.QQ://qq
                            activityMyThridPartyBinding.tvQQBinding.setText("解绑");
                            isQQBing = true;
                            break;
                    }
                }else {
                    ToastUtils.shortCenterToast("绑定失败");
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
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    switch (type){
                        case GlobalConstants.LoginPlatformType.WECHAT://微信
                            activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
                            isWinxinBing = false;
                            break;
                        case GlobalConstants.LoginPlatformType.QQ://qq
                            activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
                            isQQBing = false;
                            break;
                    }
                }else {
                    ToastUtils.shortCenterToast("解绑失败");
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
