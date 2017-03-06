package com.slash.youth.engine;

import android.app.Activity;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.CheckVersionProtocol;
import com.slash.youth.http.protocol.GetPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.LoginGetTagProtocol;
import com.slash.youth.http.protocol.LoginSetAvatarProtocol;
import com.slash.youth.http.protocol.LoginSetIndustryAndDirectionProtocol;
import com.slash.youth.http.protocol.LoginSetRealnameProtocol;
import com.slash.youth.http.protocol.LoginSetTagProtocol;
import com.slash.youth.http.protocol.LoginoutProtocol;
import com.slash.youth.http.protocol.PhoneLoginpProtocol;
import com.slash.youth.http.protocol.ThirdPartyLoginProtocol;
import com.slash.youth.http.protocol.TokenLoginProtocol;
import com.slash.youth.http.protocol.UpdatePhoneVerificationCodeProtocol;
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
    public static long currentLoginUserId;//实际应该在登录状态中获取
    public static String currentLoginUserName;
    public static String currentLoginUserAvatar;
    public static String currentLoginUserPhone;
    public static String currentLoginUserIndustry;
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

    //验证手机上收到的验证码
    public static void checkPhoneVerificationCode(BaseProtocol.IResultExecutor checkPhoneVerificationCode, String phoneNum, String pin) {
        CheckPhoneVerificationCodeProtocol checkPhoneVerificationCodeProtocol = new CheckPhoneVerificationCodeProtocol(phoneNum, pin);
        checkPhoneVerificationCodeProtocol.getDataFromServer(checkPhoneVerificationCode);
    }

    //变更手机页
    public static void UpdatePhoneVerificationCodeProtocol(BaseProtocol.IResultExecutor onUpdatePhoneVerificationCodeProtocol, String phoneNum, String pin) {
        UpdatePhoneVerificationCodeProtocol updatePhoneVerificationCodeProtocol = new UpdatePhoneVerificationCodeProtocol(phoneNum, pin);
        updatePhoneVerificationCodeProtocol.getDataFromServer(onUpdatePhoneVerificationCodeProtocol);
    }


    /**
     * 手机号登录u
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
     * @param wechatOpenid              后面新增的参数，微信登录时传openid，为了兼容web端
     */
    public static void serverThirdPartyLogin(BaseProtocol.IResultExecutor onThirdPartyLoginFinished, String _3pToken, String _3pUid, String loginPlatform, String wechatOpenid) {
        ThirdPartyLoginProtocol thirdPartyLoginProtocol = new ThirdPartyLoginProtocol(_3pToken, _3pUid, loginPlatform, wechatOpenid);
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
     * 登录后 完善技能标签,设置行业和方向（一级和二级标签）
     */
    public static void loginSetIndustryAndDirection(BaseProtocol.IResultExecutor onSetTagFinished, String industry, String direction) {
        LoginSetIndustryAndDirectionProtocol loginSetIndustryAndDirectionProtocol = new LoginSetIndustryAndDirectionProtocol(industry, direction);
        loginSetIndustryAndDirectionProtocol.getDataFromServer(onSetTagFinished);
    }


    /**
     * 登录后 完善技能标签,设置用户的技能标签（三级标签）
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
//                    String uid = data.get("uid");
                    String uid = data.get("openid");
                    SpUtils.setString("QQ_token", QQ_access_token);
                    SpUtils.setString("QQ_uid", uid);
                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String openid = data.get("unionid");
//                    String openid = data.get("openid");
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

    //版本检测
    public static void checkVersion(BaseProtocol.IResultExecutor onCheckVersion, int type, long code) {
        CheckVersionProtocol checkVersionProtocol = new CheckVersionProtocol(type, code);
        checkVersionProtocol.getDataFromServer(onCheckVersion);
    }

    //登出
    public static void logout(BaseProtocol.IResultExecutor onLogout, String token) {
        LoginoutProtocol loginoutProtocol = new LoginoutProtocol(token);
        loginoutProtocol.getDataFromServer(onLogout);
    }

    /**
     * 清空SharePreferences中的登录信息,解决token时未完成个人信息的注册账号直接进入首页的问题
     */
    public static void clearSpLoginInfo() {
        SpUtils.setLong("uid", 0);
        SpUtils.setString("token", "");
        SpUtils.setString("rongToken", "");
    }

}




