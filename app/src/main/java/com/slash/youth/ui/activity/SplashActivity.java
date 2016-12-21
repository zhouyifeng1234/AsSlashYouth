package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.domain.TokenLoginResultBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

/**
 * Created by zhouyifeng on 2016/12/11.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView ivSplash = new ImageView(CommonUtils.getContext());
        ivSplash.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams ll = new RandomLayout.LayoutParams(-1, -1);
        ivSplash.setLayoutParams(ll);
        ivSplash.setImageResource(R.mipmap.splash);
        setContentView(ivSplash);

        CommonUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tokenLogin();
            }
        }, 3000);


    }


    /**
     * token自动登录
     */
    private void tokenLogin() {
        final String rongToken = SpUtils.getString("rongToken", "");//通过融云connect方法去检测是否还有效，如果有效，就直接连接，如果无效，重新从服务端接口获取
        final long uid = SpUtils.getLong("uid", -1);
        final String token = SpUtils.getString("token", "");
        LoginManager.token = token;
        LogKit.v("token:" + token + " uid:" + uid);
        if (!TextUtils.isEmpty(token)) {
            //如果本地保存的token不为空，尝试用token自动登录
            LoginManager.tokenLogin(new BaseProtocol.IResultExecutor<TokenLoginResultBean>() {
                @Override
                public void execute(TokenLoginResultBean dataBean) {
                    //页面跳转
                    if (uid != -1) {
                        LogKit.v("token登录成功，直接跳转到首页");
                        LoginManager.currentLoginUserId = uid;
                        LoginManager.token = token;
                        LoginManager.rongToken = rongToken;
                        //链接融云
                        MsgManager.connectRongCloud(LoginManager.rongToken);

                        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
                        startActivity(intentHomeActivity);
                        finish();
                    } else {
                        gotoLoginActivity();
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v("token登录失败");
                    gotoLoginActivity();
                }
            });
        } else {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {
        Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }
}
