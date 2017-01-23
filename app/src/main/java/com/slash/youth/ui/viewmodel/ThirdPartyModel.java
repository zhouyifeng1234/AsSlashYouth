package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
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

import java.util.List;

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
        if(isWinxinBing){//绑定
            loginUnBind(GlobalConstants.LoginPlatformType.WECHAT);
        }else {//没有绑定
            if(SpUtils.getString("WEIXIN_token", "").equals("")){
                loginManager.authorizateWEIXIN(bindThridPartyActivity);
            }
            String WEIXIN_token = SpUtils.getString("WEIXIN_token", "");
            String WEIXIN_uid = SpUtils.getString("WEIXIN_uid", "");
            loginBind(WEIXIN_token,WEIXIN_uid, GlobalConstants.LoginPlatformType.WECHAT);
        }
    }

    //qq
    public void qq(View view){
        if(isQQBing){
            loginUnBind(GlobalConstants.LoginPlatformType.QQ);
        }else {
            if(SpUtils.getString("QQ_token", "").equals("")){
                loginManager.authorizateQQ(bindThridPartyActivity);
            }
            String qq_token = SpUtils.getString("QQ_token", "");
            String qq_uid = SpUtils.getString("QQ_uid", "");

            loginBind(qq_token,qq_uid, GlobalConstants.LoginPlatformType.QQ);
        }
    }

    //微博
    public void weibo(View view){
        if(isWinboBing){
            activityMyThridPartyBinding.tvWeiboBinding.setText("去绑定");
           // LoginManager.serverThirdPartyLogin();
           loginBind(" token的值 ",GlobalConstants.ThirdAppId.APPID_WEIBO, GlobalConstants.LoginPlatformType.WEIBO);
            SpUtils.setBoolean("isWinbo",false);
        }else {
            activityMyThridPartyBinding.tvWeiboBinding.setText("解绑");
            loginUnBind(GlobalConstants.LoginPlatformType.WEIBO);
            SpUtils.setBoolean("isWinbo",true);
        }
    }

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
                        SpUtils.setBoolean("isWinxin",true);
                        break;
                    case 2://qq
                        activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
                        SpUtils.setBoolean("isQQ",true);
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
                        SpUtils.setBoolean("isWinxin",false);
                        break;
                    case 2://qq
                        activityMyThridPartyBinding.tvQQBinding.setText("解绑");
                        SpUtils.setBoolean("isQQ",false);
                        break;
                }

            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

}
