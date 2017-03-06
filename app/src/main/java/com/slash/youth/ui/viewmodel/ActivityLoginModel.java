package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.domain.CustomerServiceBean;
import com.slash.youth.domain.PhoneLoginResultBean;
import com.slash.youth.domain.RongTokenBean;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.domain.ThirdPartyLoginResultBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.HomeActivity2;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.PerfectInfoActivity;
import com.slash.youth.ui.activity.SlashProtocolActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PhoneNumUtils;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.UUID;

import io.rong.imlib.RongIMClient;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class ActivityLoginModel extends BaseObservable {
    public static final int PAGE_STATE_REGISTER = 1000;//"新手注册"状态
    public static final int PAGE_STATE_GOTOLOGIN = 1001;//"我有账号,去登录"状态

    ActivityLoginBinding mActivityLoginBinding;
    LoginActivity.QQLoginUiListener qqLoginUiListener;
    LoginActivity loginActivity;
    SsoHandler mSsoHandler;
    Activity mActivity;
    int currentPageState = PAGE_STATE_GOTOLOGIN;//当前的页面状态，默认为"我有账号,去登录"状态


    public ActivityLoginModel(ActivityLoginBinding activityLoginBinding, LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity, SsoHandler ssoHandler, Activity activity) {
        this.mActivityLoginBinding = activityLoginBinding;
        this.qqLoginUiListener = qqLoginUiListener;
        this.loginActivity = loginActivity;
        this.mSsoHandler = ssoHandler;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
    }

    private void initView() {
//        setRegisterAndLoginTextVisibility();
    }

    /**
     * 点击“遇到问题”，与斜杠小助手聊天
     *
     * @param v
     */
    public void chatToSlashHelper(View v) {
        final String tmpRongToken = SpUtils.getString("tmpRongToken", "");
        if (TextUtils.isEmpty(tmpRongToken)) {
            UUID uuid = UUID.randomUUID();
            String tmpUid = uuid.toString();//创建一个临时的uid，用来获取临时的融云token
            LogKit.v("tmpUid:" + tmpUid);
            MsgManager.getRongToken(new BaseProtocol.IResultExecutor<RongTokenBean>() {
                @Override
                public void execute(RongTokenBean dataBean) {
                    String newTmpRongToken = dataBean.data.token;
                    SpUtils.setString("tmpRongToken", newTmpRongToken);
                    connectToRongCloud(newTmpRongToken);
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取临时融云token失败");
                }
            }, tmpUid, "11");
        } else {
            connectToRongCloud(tmpRongToken);
        }
    }

//    private int connectToRoungCloudCount = 0;

    private void connectToRongCloud(String tmpRongToken) {
        RongIMClient.connect(tmpRongToken, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
//                connectToRoungCloudCount++;
//                if (connectToRoungCloudCount > 3) {
//                    ToastUtils.shortToast("使用临时token链接融云失败");
//                } else {
//                    connectToRongCloud();
//                }
            }

            @Override
            public void onSuccess(String s) {
                gotoChatSlashHelperActivity();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void gotoChatSlashHelperActivity() {
        final Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
//        intentChatActivity.putExtra("targetId", "1000");
        long customerServiceUid = MsgManager.getCustomerServiceUidFromSp();
        if (customerServiceUid > 0) {
            //sp中保存有随机客服的uid
            MsgManager.customerServiceUid = customerServiceUid + "";
            intentChatActivity.putExtra("targetId", customerServiceUid + "");
            mActivity.startActivity(intentChatActivity);
        } else {
            MsgManager.getCustomerService(new BaseProtocol.IResultExecutor<CustomerServiceBean>() {
                @Override
                public void execute(CustomerServiceBean dataBean) {
                    long customerServiceUid = dataBean.data.uid;
                    MsgManager.customerServiceUid = customerServiceUid + "";
                    intentChatActivity.putExtra("targetId", customerServiceUid + "");
                    mActivity.startActivity(intentChatActivity);
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取客服UID失败:" + result);
                }
            });
        }
    }

    /**
     * 登录按钮点击事件
     *
     * @param v
     */
    public void login(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_CLICK_ENTER);
        if (!isCheckedSlashProtocol) {
            ToastUtils.shortToast("请同意用户协议");
            return;
        }

        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
        String pin = mActivityLoginBinding.etActivityLoginVerificationCode.getText().toString();
        if (TextUtils.isEmpty(phoenNum) || TextUtils.isEmpty(pin)) {
            ToastUtils.shortToast("手机号或者验证码不能为空");
            return;
        }
        boolean isCorrect = PhoneNumUtils.checkPhoneNum(phoenNum);
        if (!isCorrect) {
            ToastUtils.shortToast("请输入正确的手机号码");
            return;
        }
        LoginManager.phoneLogin(new BaseProtocol.IResultExecutor<PhoneLoginResultBean>() {
            @Override
            public void execute(PhoneLoginResultBean dataBean) {

                //增加验证码错误提示
                if (dataBean.rescode == 7) {
                    ToastUtils.shortToast("验证码错误");
                    return;
                }

                //如果登录失败，dataBean.data可能是null  {  "rescode": 7  }
                if (dataBean.data == null) {
                    ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                    return;
                }

                String rongToken = dataBean.data.rongToken;//融云token
                String token = dataBean.data.token;
                long uid = dataBean.data.uid;

                if (dataBean.rescode == 0) {
                    //登陆成功，老用户
                    savaLoginState(uid, token, rongToken);

                    //链接融云
                    MsgManager.connectRongCloud(rongToken);

                    SpUtils.setBoolean("showMoreDemandDialog", false);

                    //这里需要判断是否设置了个人信息和技能标签，来跳转不同的页面
                    oldUserInfoCheck();
                } else if (dataBean.rescode == 11) {
                    //登陆成功，新用户
                    savaLoginState(uid, token, rongToken);

                    //链接融云
                    MsgManager.connectRongCloud(rongToken);

                    SpUtils.setBoolean("showMoreDemandDialog", true);

                    Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
                    mActivity.startActivity(intentPerfectInfoActivity);
                } else {
                    ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
                //这里不会执行
            }
        }, phoenNum, pin, "", "");

        //TODO 具体的登录逻辑，等服务端相关接口完成以后再实现
//                Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
//        intentPerfectInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentPerfectInfoActivity);

//        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentHomeActivity);

//        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
//        String pin = mActivityLoginBinding.etActivityLoginVerificationCode.getText().toString();
//        LoginManager.checkPhoneVerificationCode(phoenNum, pin);

        //这里跳转至聊天界面只是为了测试聊天界面

     /*   Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
        intentChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentChatActivity);
*/

        // Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
////        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
//
//        Bundle taskInfoBundle = new Bundle();
//        taskInfoBundle.putLong("tid", 111);
//        taskInfoBundle.putInt("type", 1);
//        taskInfoBundle.putString("title", "APP开发");
//        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
//
//        intentChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentChatActivity);


//        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
////        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
//
//        Bundle taskInfoBundle = new Bundle();
//        taskInfoBundle.putLong("tid", 111);
//        taskInfoBundle.putInt("type", 1);
//        taskInfoBundle.putString("title", "APP开发");
//        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
//
//        intentChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentChatActivity);

    }

    /**
     * 根据是否设置了个人信息（真实姓名）和技能标签来跳转到不同的页面
     */
    private void oldUserInfoCheck() {
        //这里需要真实姓名、用户的一级、二级和三级技能标签来做判断
        UserInfoEngine.getMyUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                String realName = uinfo.name;
                if (TextUtils.isEmpty(realName)) {
                    Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
                    mActivity.startActivity(intentPerfectInfoActivity);
                } else {
                    String industry = uinfo.industry;
                    String direction = uinfo.direction;
                    String tag = uinfo.tag;
                    if (!TextUtils.isEmpty(industry) && !TextUtils.isEmpty(direction) && !TextUtils.isEmpty(tag)) {
//                        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//                        mActivity.startActivity(intentHomeActivity);
                        Intent intentHomeActivity2 = new Intent(CommonUtils.getContext(), HomeActivity2.class);
                        mActivity.startActivity(intentHomeActivity2);
                        if (LoginActivity.activity != null) {
                            LoginActivity.activity.finish();
                            LoginActivity.activity = null;
                        }
                    } else {
                        Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
                        mActivity.startActivity(intentChooseSkillActivity);
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取个人信息失败:" + result);
            }
        });
    }

    /**
     * @param uid
     * @param token
     * @param rongToken
     */
    private void savaLoginState(long uid, String token, String rongToken) {
        LoginManager.currentLoginUserId = uid;
        LoginManager.token = token;
        LoginManager.rongToken = rongToken;

        SpUtils.setLong("uid", uid);
        SpUtils.setString("token", token);
        SpUtils.setString("rongToken", rongToken);
    }

    boolean isSendPin = false;

    public void sendPhoneVerificationCode(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_CLICK_VERTIFYCODE);

        if (isSendPin) {
            return;
        }

        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
        if (TextUtils.isEmpty(phoenNum)) {
            ToastUtils.shortToast("请输入手机号");
            return;
        }
        boolean isCorrect = PhoneNumUtils.checkPhoneNum(phoenNum);
        if (!isCorrect) {
            ToastUtils.shortToast("请输入正确的手机号码");
            return;
        }
        LogKit.v(phoenNum);
        isSendPin = true;
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(new BaseProtocol.IResultExecutor<SendPinResultBean>() {
            @Override
            public void execute(SendPinResultBean dataBean) {
//                ToastUtils.shortToast("获取验证码成功");
                ToastUtils.shortToast("验证码已发送，请查收");
                isSendPin = true;
                pinSecondsCount = 61;
                CommonUtils.getHandler().post(new PinCountDown());
            }

            @Override
            public void executeResultError(String result) {
//                ToastUtils.shortToast("获取验证码失败");
                isSendPin = false;
                ToastUtils.shortToast("验证码发送失败");
            }
        }, phoenNum);
    }

    public void wechatLogin(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_CLICK_WECHAT_ENTER);

        //  LoginManager.loginWeChat();
        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(loginActivity, mPermissionList, 123);
        }
    }

    public void qqLogin(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_CLICK_QQ_ENTER);

//        LoginManager.loginQQ(qqLoginUiListener, loginActivity);
        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        if (mShareAPI.isInstall(loginActivity, SHARE_MEDIA.QQ)) {
            mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtils.shortToast("请先安装QQ客户端");
        }
    }

    public void weiboLogin(View v) {
//        mSsoHandler.authorize(new SlashWeiboAuthListener());

        //验证包名和签名,是微信那边的
        UmengTool.getSignature(loginActivity);

       /* UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.SINA, umAuthListener);*/
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
                LogKit.v("weibo token:" + token + "    weibo QQ_uid:" + uid);
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

    String QQ_access_token;//17301782584  18915521461
    String QQ_uid;//这个值应该取openid,而不是uid,为了避免大幅度改代码，不改变变量名
    String WEIXIN_access_token;
    String WEIXIN_unionid;//这个值应该取openid,而不是unionid,为了避免大幅度改代码，不改变变量名
    String WEIXIN_openid;//微信为了和WEB兼容，增加openid参数

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
            UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
            switch (platform) {
                case QQ:
                    LogKit.v("qq data size:" + data.size());
                    for (String key : data.keySet()) {
                        LogKit.v("-----------QQ Login------------" + key + ":" + data.get(key));
                    }
                    QQ_access_token = data.get("access_token");
//                    QQ_uid = data.get("uid");
                    QQ_uid = data.get("openid");//通过日志发现， 这里的uid和openid是一样的
                    SpUtils.setString("QQ_token", QQ_access_token);
                    SpUtils.setString("QQ_uid", QQ_uid);
                    LogKit.v("QQ_access_token:" + QQ_access_token + " QQ_uid:" + QQ_uid);
                    if (TextUtils.isEmpty(QQ_access_token) || TextUtils.isEmpty(QQ_uid)) {
                        ToastUtils.shortToast("QQ登录失败");
                        return;
                    }

                    mShareAPI.getPlatformInfo(loginActivity, SHARE_MEDIA.QQ, umAuthListenerForUserInfo);
                    break;
                case WEIXIN:
                    LogKit.v("weixin data size:" + data.size());
                    for (String key : data.keySet()) {
                        LogKit.v("-----------WEIXIN Login------------" + key + ":" + data.get(key));
                    }
                    WEIXIN_access_token = data.get("access_token");
                    WEIXIN_unionid = data.get("unionid");
                    WEIXIN_openid = data.get("openid");//微信为了和WEB兼容，增加openid参数
                    SpUtils.setString("WEIXIN_token", WEIXIN_access_token);
                    SpUtils.setString("WEIXIN_uid", WEIXIN_unionid);
                    SpUtils.setString("WEIXIN_openid", WEIXIN_openid);
                    LogKit.v("WEIXIN_access_token:" + WEIXIN_access_token + " WEIXIN_unionid:" + WEIXIN_unionid + " WEIXIN_openid:" + WEIXIN_openid);
                    if (TextUtils.isEmpty(WEIXIN_access_token) || TextUtils.isEmpty(WEIXIN_unionid)) {
                        ToastUtils.shortToast("微信登录失败");
                        return;
                    }

                    mShareAPI.getPlatformInfo(loginActivity, SHARE_MEDIA.WEIXIN, umAuthListenerForUserInfo);
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                        ActivityCompat.requestPermissions(loginActivity, mPermissionList, 123);
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

    String QQ_nickname;
    String QQ_gender;
    String QQ_avatar;
    String QQ_province;
    String QQ_city;
    String WEIXIN_nickname;
    String WEIXIN_gender;
    String WEIXIN_avatar;
    String WEIXIN_province;
    String WEIXIN_city;

    private UMAuthListener umAuthListenerForUserInfo = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            ToastUtils.shortToast("GetUserInfo succeed");
            LogKit.v("GetUserInfo succeed");
            switch (platform) {
                case QQ:
                    LogKit.v("qq data size:" + data.size());
                    for (String key : data.keySet()) {
                        LogKit.v("-----------QQ UserInfo------------" + key + ":" + data.get(key));
                    }
                    QQ_nickname = data.get("screen_name");
                    String gender = data.get("gender");
                    if (gender.equals("男")) {
                        QQ_gender = "1";
                    } else {
                        QQ_gender = "2";
                    }
                    QQ_avatar = data.get("profile_image_url");
                    QQ_province = data.get("province");
                    QQ_city = data.get("city");
//                    name = data.get("screen_name");
//                    gender = data.get("gender");
//                    city = data.get("city");
//                    LogKit.v("name:" + name + "  gender:" + gender + "  city:" + city);
                    LogKit.v("QQ_access_token:" + QQ_access_token + "  QQ_uid:" + QQ_uid);
                    thirdLoginPlatformType = 2;
                    LoginManager.serverThirdPartyLogin(onThirdPartyLoginFinished, QQ_access_token, QQ_uid, thirdLoginPlatformType + "", null);
                    break;
                case WEIXIN:
                    LogKit.v("weixin data size:" + data.size());
                    for (String key : data.keySet()) {
                        LogKit.v("-----------WEIXIN UserInfo------------" + key + ":" + data.get(key));
                    }
                    WEIXIN_nickname = data.get("screen_name");
                    WEIXIN_gender = data.get("gender");
                    WEIXIN_avatar = data.get("profile_image_url");
                    WEIXIN_province = data.get("province");
                    WEIXIN_city = data.get("city");

//                    name = data.get("screen_name");
//                    gender = data.get("gender");
//                    city = data.get("city");
//                    LogKit.v("name:" + name + "  gender:" + gender + "  city:" + city);
                    LogKit.v("WEIXIN_access_token:" + WEIXIN_access_token + "  WEIXIN_unionid:" + WEIXIN_unionid);
                    thirdLoginPlatformType = 1;
                    LoginManager.serverThirdPartyLogin(onThirdPartyLoginFinished, WEIXIN_access_token, WEIXIN_unionid, thirdLoginPlatformType + "", WEIXIN_openid);
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            ToastUtils.shortToast("GetUserInfo fail");
            LogKit.v("GetUserInfo fail");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            ToastUtils.shortToast("GetUserInfo cancel");
            LogKit.v("GetUserInfo cancel");
        }
    };

    private int thirdLoginPlatformType = -1;//WECHAT = 1    QQ = 2    WEIBO = 3

    public BaseProtocol.IResultExecutor onThirdPartyLoginFinished = new BaseProtocol.IResultExecutor<ThirdPartyLoginResultBean>() {
        @Override
        public void execute(ThirdPartyLoginResultBean dataBean) {
            if (dataBean.rescode == 9) {//新用户，需要绑定手机号
                LogKit.v("新用户，需要绑定手机号");
                String _3ptoken = dataBean.data.token;
                if(_3ptoken.split("&").length<=3){
                    _3ptoken=_3ptoken+"&"+ WEIXIN_openid;
                }

                Bundle thirdPlatformBundle = new Bundle();
                thirdPlatformBundle.putString("_3ptoken", _3ptoken);
                String userInfo = "";
                if (thirdLoginPlatformType == 1) {
                    //微信登录
                    userInfo = WEIXIN_nickname + "&" + WEIXIN_gender + "&" + WEIXIN_avatar + "&" + WEIXIN_province + "&" + WEIXIN_city;
                } else if (thirdLoginPlatformType == 2) {
                    //QQ登录
                    userInfo = QQ_nickname + "&" + QQ_gender + "&" + QQ_avatar + "&" + QQ_province + "&" + QQ_city;
                } else {
                    LogKit.v("第三方平台 type 编号错误");
                    ToastUtils.shortToast("第三方平台 type 编号错误");
                }
                thirdPlatformBundle.putString("userInfo", userInfo);

                Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
                intentPerfectInfoActivity.putExtras(thirdPlatformBundle);
                mActivity.startActivity(intentPerfectInfoActivity);
            } else if (dataBean.rescode == 0) {//已经登录过的用户
                LogKit.v("已经登录过的用户");
                String token = dataBean.data.token;
                long uid = dataBean.data.uid;
                LoginManager.token = token;
                LoginManager.currentLoginUserId = uid;
                SpUtils.setString("token", token);
                SpUtils.setLong("uid", uid);
                //获取融云token，并连接融云
                MsgManager.getRongToken(new BaseProtocol.IResultExecutor<RongTokenBean>() {
                    @Override
                    public void execute(RongTokenBean dataBean) {
                        String rongToken = dataBean.data.token;
                        LoginManager.rongToken = rongToken;
                        SpUtils.setString("rongToken", rongToken);
                        //这里可能需要先断开和融云的链接，然后重新再链接
                        MsgManager.connectRongCloud(rongToken);
                    }

                    @Override
                    public void executeResultError(String result) {
                        ToastUtils.shortToast("第三方登录，获取融云token失败:" + result);
                    }
                }, uid + "", "111");//这里的phone随便传一直值

                //跳转到首页
//                Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//                mActivity.startActivity(intentHomeActivity);
//                if (LoginActivity.activity != null) {
//                    LoginActivity.activity.finish();
//                    LoginActivity.activity = null;
//                }
                //这里需要判断是否完善里技能标签
                thirdOldUserCheck();
            } else {
                LogKit.v("服务端第三方登录失败");
                ToastUtils.shortToast("服务端第三方登录失败");
            }
        }

        @Override
        public void executeResultError(String result) {
            //这里不会执行
        }
    };

    private void thirdOldUserCheck() {
        UserInfoEngine.getMyUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                String industry = uinfo.industry;
                String direction = uinfo.direction;
                String tag = uinfo.tag;
                if (!TextUtils.isEmpty(industry) && !TextUtils.isEmpty(direction) && !TextUtils.isEmpty(tag)) {
//                    Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
//                    mActivity.startActivity(intentHomeActivity);
                    Intent intentHomeActivity2 = new Intent(CommonUtils.getContext(), HomeActivity2.class);
                    mActivity.startActivity(intentHomeActivity2);
                    if (LoginActivity.activity != null) {
                        LoginActivity.activity.finish();
                        LoginActivity.activity = null;
                    }
                } else {
                    Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
                    mActivity.startActivity(intentChooseSkillActivity);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("thirdOldUserCheck 获取用户个人信息失败:" + result);
            }
        });
    }

    boolean isCheckedSlashProtocol = true;

    public void checkSlashProtocol(View v) {
        if (isCheckedSlashProtocol) {//选中变成不选中
            mActivityLoginBinding.ivCheckProtocolIcon.setImageResource(R.mipmap.xz_no_icon);
        } else {//不选中变成选中
            mActivityLoginBinding.ivCheckProtocolIcon.setImageResource(R.mipmap.xz_icon);
        }

        isCheckedSlashProtocol = !isCheckedSlashProtocol;
    }

    public void viewSlashProtocol(View v) {
        Intent intentSlashProtocolActivity = new Intent(CommonUtils.getContext(), SlashProtocolActivity.class);
        mActivity.startActivity(intentSlashProtocolActivity);
    }

    int pinSecondsCount;

    private class PinCountDown implements Runnable {

        @Override
        public void run() {
            pinSecondsCount--;
            if (pinSecondsCount < 0) {
                mActivityLoginBinding.btnSendpinText.setText("验证码");
                mActivityLoginBinding.btnSendpinText.setBackgroundResource(R.drawable.btn_send_pin_blue);
                isSendPin = false;
            } else {
                mActivityLoginBinding.btnSendpinText.setText(pinSecondsCount + "S");
                mActivityLoginBinding.btnSendpinText.setBackgroundResource(R.drawable.btn_send_pin_gray);
                CommonUtils.getHandler().postDelayed(this, 1000);
            }
        }
    }
}
