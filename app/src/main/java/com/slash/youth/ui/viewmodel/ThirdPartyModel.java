package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.slash.youth.BR;
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
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
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
    private int unBindVisibility = View.GONE;

    public ThirdPartyModel(ActivityMyThridPartyBinding activityMyThridPartyBinding, BindThridPartyActivity bindThridPartyActivity) {
        this.activityMyThridPartyBinding = activityMyThridPartyBinding;
        this.bindThridPartyActivity = bindThridPartyActivity;
        loginManager = new LoginManager();
        initData();
    }

    private void initData() {
        bindPlatform(LoginManager.token);
    }

    private void initView(boolean isWinxinBing, boolean isQQBing, boolean isWinboBing) {
        if (isWinxinBing) {
            activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
        } else {
            activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
        }

        if (isQQBing) {
            activityMyThridPartyBinding.tvQQBinding.setText("解绑");
        } else {
            activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
        }
    }

    @Bindable
    public int getUnBindVisibility() {
        return unBindVisibility;
    }

    public void setUnBindVisibility(int unBindVisibility) {
        this.unBindVisibility = unBindVisibility;
        notifyPropertyChanged(BR.unBindVisibility);
    }

    public void cannelDialog(View view) {
        setUnBindVisibility(View.GONE);
    }

    public void sureDialog(View view) {
        setUnBindVisibility(View.GONE);
        switch (type) {
            case GlobalConstants.LoginPlatformType.WECHAT:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_WECHAT_UNBINDING_CONFIRM_UNBUNDING);
                loginUnBind(GlobalConstants.LoginPlatformType.WECHAT);
                break;
            case GlobalConstants.LoginPlatformType.QQ:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_QQ_UNBINDING_CONFIRM_UNBUNDING);
                loginUnBind(GlobalConstants.LoginPlatformType.QQ);
                break;
        }
    }

    //绑定平台
    public void bindPlatform(String token) {
        GetBindProtocol getBindProtocol = new GetBindProtocol(token);
        getBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<GetBindBean>() {
            @Override
            public void execute(GetBindBean dataBean) {
                int rescode = dataBean.getRescode();
                if (rescode == 0) {
                    GetBindBean.DataBean data = dataBean.getData();
                    List<String> platforms = data.getPlatforms();
                    for (String platform : platforms) {
                        if (platform.equals("1")) {
                            isWinxinBing = true;
                            //type = GlobalConstants.LoginPlatformType.WECHAT;
                        } else if (platform.equals("2")) {
                            isQQBing = true;
                            //type = GlobalConstants.LoginPlatformType.QQ;
                        } else if (platform.equals("3")) {
                            isWinboBing = true;
                            //type = GlobalConstants.LoginPlatformType.WEIBO;
                        }
                    }
                    initView(isWinxinBing, isQQBing, isWinboBing);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    //微信
    public void weixin(View view) {
        type = GlobalConstants.LoginPlatformType.WECHAT;
        if (isWinxinBing) {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_WECHAT_UNBINDING);
            setUnBindVisibility(View.VISIBLE);
        } else {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_WECHAT_BINDING);
            UMShareAPI mShareAPI = UMShareAPI.get(bindThridPartyActivity);
            mShareAPI.doOauthVerify(bindThridPartyActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(bindThridPartyActivity, mPermissionList, 123);
            }
        }
    }

    //qq
    public void qq(View view) {
        type = GlobalConstants.LoginPlatformType.QQ;
        if (isQQBing) {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_QQ_UNBINDING);
            setUnBindVisibility(View.VISIBLE);
        } else {
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT_QQ_BINDING);
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
                    String QQ_access_token = data.get("access_token");
//                    String QQ_uid = data.get("uid");
                    String QQ_uid = data.get("openid");
                    loginBind(QQ_access_token, QQ_uid, GlobalConstants.LoginPlatformType.QQ, null);
                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String unionid = data.get("unionid");
                    String openid = data.get("openid");
                    loginBind(WEIXIN_access_token, unionid, GlobalConstants.LoginPlatformType.WECHAT, openid);
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
    public void loginBind(String token, String uid, Byte loginPlatform, String wechatOpenid) {
        final LoginBindProtocol loginBindProtocol = new LoginBindProtocol(token, uid, loginPlatform, wechatOpenid);
        loginBindProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<LoginBindBean>() {
            @Override
            public void execute(LoginBindBean dataBean) {
                int rescode = dataBean.getRescode();
                if (rescode == 0) {
                    switch (type) {
                        case GlobalConstants.LoginPlatformType.WECHAT://微信
                            activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
                            isWinxinBing = true;
                            break;
                        case GlobalConstants.LoginPlatformType.QQ://qq
                            activityMyThridPartyBinding.tvQQBinding.setText("解绑");
                            isQQBing = true;
                            break;
                    }
                } else {
                    ToastUtils.shortCenterToast("绑定失败");
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
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
                if (rescode == 0) {
                    switch (type) {
                        case GlobalConstants.LoginPlatformType.WECHAT://微信
                            activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
                            isWinxinBing = false;
                            break;
                        case GlobalConstants.LoginPlatformType.QQ://qq
                            activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
                            isQQBing = false;
                            break;
                    }
                } else {
                    ToastUtils.shortCenterToast("解绑失败");
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    //微博
    public void weibo(View view) {
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
