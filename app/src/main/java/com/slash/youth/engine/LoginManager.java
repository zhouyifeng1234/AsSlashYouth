package com.slash.youth.engine;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.GetPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.PhoneLogin;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

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

    public static IWXAPI iwxApi;
    public static Tencent mTencent;

    static {
        //微信初始化
        iwxApi = WXAPIFactory.createWXAPI(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WECHAT, true);
        iwxApi.registerApp(GlobalConstants.ThirdAppId.APPID_WECHAT);

        //QQ初始化
        mTencent = Tencent.createInstance(GlobalConstants.ThirdAppId.APPID_QQ, CommonUtils.getContext());
    }

    //调用发送手机验证码接口，将验证码发送到手机上
    public static void getPhoneVerificationCode(String phoneNum) {
        GetPhoneVerificationCodeProtocol getPhoneVerificationCodeProtocol = new GetPhoneVerificationCodeProtocol(phoneNum);
        getPhoneVerificationCodeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
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

    public static void loginPhoneNum(String phone, String pin, String _3pToken, String userInfo) {
        PhoneLogin phoneLogin = new PhoneLogin(phone, pin, _3pToken, userInfo);
        phoneLogin.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {

            @Override
            public void execute(String dataBean) {
//                LogKit.v(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        });
    }

    public boolean Login() {
        //TODO 具体的登录逻辑，判断是否登录成功


        return true;
    }


    /**
     * 第三方 微信登录
     */
    public static void loginWeChat() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "slash_youth_login";
        iwxApi.sendReq(req);

    }

    /**
     * 第三方 QQ登录
     */
    public static void loginQQ(LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity) {
//        if (!mTencent.isSessionValid())
//        {
//            mTencent.login(this, Scope, listener);
//        }
        mTencent.login(loginActivity, "all", qqLoginUiListener);
    }

    /**
     * 第三方 新浪微博登录
     */
    public static void loginWeiBo() {

    }

}
