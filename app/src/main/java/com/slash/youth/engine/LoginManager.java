package com.slash.youth.engine;

import android.app.Activity;

import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.GetPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.LoginGetTagProtocol;
import com.slash.youth.http.protocol.LoginSetAvatarProtocol;
import com.slash.youth.http.protocol.LoginSetRealnameProtocol;
import com.slash.youth.http.protocol.LoginSetTagProtocol;
import com.slash.youth.http.protocol.PhoneLoginpProtocol;
import com.slash.youth.http.protocol.ThirdPartyLoginProtocol;
import com.slash.youth.http.protocol.TokenLoginProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LoginManager {
    public static long currentLoginUserId = 10000;//实际应该在登录状态中获取
    public static String currentLoginUserName = "风";
    public static String currentLoginUserAvatar = "group1/M00/00/02/eBtfY1g68l2AYPzzAACY_JV8bdw.7ad5ad";
    //        public static long currentLoginUserId = 10002;//实际应该在登录状态中获取
//    public static String currentLoginUserName = "jim";
//    public static String currentLoginUserAvatar = "group1/M00/00/02/eBtfY1g68kiAfiCNAABuHg0Rbxs.0a9ae1";
    public static String token = "";//我们自己服务器登录后的toekn
    public static String rongToken = "";//融云的token


    public static IWXAPI iwxApi;
    public static Tencent mTencent;

    static {
        //微信初始化
        iwxApi = WXAPIFactory.createWXAPI(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WECHAT, true);
        iwxApi.registerApp(GlobalConstants.ThirdAppId.APPID_WECHAT);

        //QQ初始化
        mTencent = Tencent.createInstance(GlobalConstants.ThirdAppId.APPID_QQ, CommonUtils.getContext());
    }


    /**
     * 调用发送手机验证码接口，将验证码发送到手机上
     *
     * @param onSendPinFinished
     * @param phoneNum
     */
    public static void getPhoneVerificationCode(BaseProtocol.IResultExecutor onSendPinFinished, String phoneNum) {
        GetPhoneVerificationCodeProtocol getPhoneVerificationCodeProtocol = new GetPhoneVerificationCodeProtocol(phoneNum);
        getPhoneVerificationCodeProtocol.getDataFromServer(onSendPinFinished);
    }

    public static void getPhoneVerificationCode(String phoneNum) {
        GetPhoneVerificationCodeProtocol getPhoneVerificationCodeProtocol = new GetPhoneVerificationCodeProtocol(phoneNum);
        getPhoneVerificationCodeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SendPinResultBean>() {
            @Override
            public void execute(SendPinResultBean dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        });
    }

    //验证手机上收到的验证码
    public static void checkPhoneVerificationCode(String phoneNum, String pin) {
        CheckPhoneVerificationCodeProtocol checkPhoneVerificationCodeProtocol = new CheckPhoneVerificationCodeProtocol(phoneNum, pin);
        checkPhoneVerificationCodeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        });
    }

    /**
     * 手机号登录
     *
     * @param phone
     * @param pin
     * @param _3pToken
     * @param userInfo
     */
    public static void phoneLogin(BaseProtocol.IResultExecutor onPhoneLoginFinished, String phone, String pin, String _3pToken, String userInfo) {
        PhoneLoginpProtocol phoneLoginpProtocol = new PhoneLoginpProtocol(phone, pin, _3pToken, userInfo);
        phoneLoginpProtocol.getDataFromServer(onPhoneLoginFinished);
    }

    /**
     * token登录
     */
    public static void tokenLogin(BaseProtocol.IResultExecutor onTokenLoginFinished) {
        TokenLoginProtocol tokenLoginProtocol = new TokenLoginProtocol();
        tokenLoginProtocol.getDataFromServer(onTokenLoginFinished);
    }

    /**
     * @param onThirdPartyLoginFinished
     * @param _3pToken                  第三方平台返回的token
     * @param _3pUid                    第三方平台返回的uid
     * @param loginPlatform             第三方平台类型     WECHAT = 1    QQ = 2    WEIBO = 3
     */
    public static void serverThirdPartyLogin(BaseProtocol.IResultExecutor onThirdPartyLoginFinished, String _3pToken, String _3pUid, String loginPlatform) {
        ThirdPartyLoginProtocol thirdPartyLoginProtocol = new ThirdPartyLoginProtocol(_3pToken, _3pUid, loginPlatform);
        thirdPartyLoginProtocol.getDataFromServer(onThirdPartyLoginFinished);
    }

    /**
     * 登录后完善信息 设置头像
     */
    public static void loginSetAvatar(BaseProtocol.IResultExecutor onSetAvatarFinished, String url) {
        LoginSetAvatarProtocol loginSetAvatarProtocol = new LoginSetAvatarProtocol(url);
        loginSetAvatarProtocol.getDataFromServer(onSetAvatarFinished);
    }

    /**
     * 登录后完善信息 设置真实姓名
     */
    public static void loginSetRealname(BaseProtocol.IResultExecutor onSetRealnameFinished, String name) {
        LoginSetRealnameProtocol loginSetRealnameProtocol = new LoginSetRealnameProtocol(name);
        loginSetRealnameProtocol.getDataFromServer(onSetRealnameFinished);
    }

    /**
     * 登录后 完善技能标签,从服务端接口获取技能标签
     */
    public static void loginGetTag(BaseProtocol.IResultExecutor onGetTagFinished) {
        LoginGetTagProtocol loginSetTagProtocol = new LoginGetTagProtocol();
        loginSetTagProtocol.getDataFromServer(onGetTagFinished);
    }

    /**
     * 登录后 完善技能标签,设置用户的技能标签
     *
     * @param onSetTagFinished
     * @param listTag
     */
    public static void loginSetTag(BaseProtocol.IResultExecutor onSetTagFinished, ArrayList<String> listTag) {
        LoginSetTagProtocol loginSetTagProtocol = new LoginSetTagProtocol(listTag);
        loginSetTagProtocol.getDataFromServer(onSetTagFinished);
    }


//    public boolean Login() {
//        //TODO 具体的登录逻辑，判断是否登录成功
//
//
//        return true;
//    }


//    /**
//     * 第三方 微信登录
//     */
//    public static void loginWeChat() {
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "slash_youth_login";
//        iwxApi.sendReq(req);
//
//    }

//    /**
//     * 第三方 QQ登录
//     */
//    public static void loginQQ(LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity) {
////        if (!mTencent.isSessionValid())
////        {
////            mTencent.login(this, Scope, listener);
////        }
//        mTencent.login(loginActivity, "all", qqLoginUiListener);
//    }

//    /**
//     * 第三方 新浪微博登录
//     */
//    public static void loginWeiBo() {
//
//    }


    //第三方QQ授权
    public void authorizateQQ(Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.QQ, umAuthListener);
    }

    //微信授权
    public void authorizateWEIXIN(Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    //微博授权
  /*  public  void authorizateWEIBO( Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.SINA, umAuthListener);
    }*/


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
            switch (platform) {
                case QQ:
                    String QQ_access_token = data.get("access_token");
                    String uid = data.get("uid");
                    SpUtils.setString("QQ_token", QQ_access_token);
                    SpUtils.setString("QQ_uid", uid);
                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String openid = data.get("unionid");
                    SpUtils.setString("WEIXIN_token", WEIXIN_access_token);
                    SpUtils.setString("WEIXIN_uid", openid);
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


}




