package com.slash.youth.engine;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.CommonUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LoginManager {
    private static IWXAPI iwxApi;

    static {
        iwxApi = WXAPIFactory.createWXAPI(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WECHAT, true);
        iwxApi.registerApp(GlobalConstants.ThirdAppId.APPID_WECHAT);
    }


    public boolean Login_() {
        //TODO 具体的登录逻辑，判断是否登录成功


        return true;
    }


    /**
     * 第三方 微信登录
     */
    public void loginWeChat() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "slash_youth_login";
        iwxApi.sendReq(req);
    }

    /**
     * 第三方 QQ登录
     */
    public void loginQQ() {

    }

    /**
     * 第三方 新浪微博登录
     */
    public void loginWeiBo() {

    }
}
