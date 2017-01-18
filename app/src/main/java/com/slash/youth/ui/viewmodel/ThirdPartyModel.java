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

    public ThirdPartyModel(ActivityMyThridPartyBinding activityMyThridPartyBinding,BindThridPartyActivity bindThridPartyActivity) {
        this.activityMyThridPartyBinding = activityMyThridPartyBinding;
        this.bindThridPartyActivity = bindThridPartyActivity;
        loginManager = new LoginManager();
        initData();
        initView();
    }

    private void initData() {
        bindPlatform(LoginManager.token);
    }

    private void initView(){
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
                        }else if(platform.equals("2")){
                            isQQBing = true;

                        }else if(platform.equals("3")){
                            isWinboBing = true;
                        }
                    }
                    initView();
                }
            }
            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
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
            }
            @Override
            public void executeResultError(String result) {
            LogKit.d("result:"+result);
            }
        });
    }

    //微信
    public void weixin(View view){

        if(isWinxinBing){
            if(SpUtils.getString("WEIXIN_token", "").equals("")){
                loginManager.authorizateWEIXIN(bindThridPartyActivity);
            }
            String WEIXIN_token = SpUtils.getString("WEIXIN_token", "");
            String WEIXIN_uid = SpUtils.getString("WEIXIN_uid", "");

            loginBind(WEIXIN_token,WEIXIN_uid, GlobalConstants.LoginPlatformType.WECHAT);
            activityMyThridPartyBinding.tvWeixinBinding.setText("去绑定");
            SpUtils.setBoolean("isWinxin",false);
        }else {
            activityMyThridPartyBinding.tvWeixinBinding.setText("解绑");
            loginUnBind(GlobalConstants.LoginPlatformType.WECHAT);
            SpUtils.setBoolean("isWinxin",true);
        }
      isWinxinBing = !isWinxinBing;
    }

    //qq
    public void qq(View view){
        if(isQQBing){
            if(SpUtils.getString("QQ_token", "").equals("")){
                loginManager.authorizateQQ(bindThridPartyActivity);
            }
            String qq_token = SpUtils.getString("QQ_token", "");
            String qq_uid = SpUtils.getString("QQ_uid", "");
            activityMyThridPartyBinding.tvQQBinding.setText("去绑定");
            loginBind(qq_token,qq_uid, GlobalConstants.LoginPlatformType.QQ);
            SpUtils.setBoolean("isQQ",false);

        }else {
            activityMyThridPartyBinding.tvQQBinding.setText("解绑");
            loginUnBind(GlobalConstants.LoginPlatformType.QQ);
            SpUtils.setBoolean("isQQ",true);
        }
        isQQBing = !isQQBing;
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
        isWinboBing = !isWinboBing;
    }

}
