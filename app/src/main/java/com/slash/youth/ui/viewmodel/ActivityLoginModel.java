package com.slash.youth.ui.viewmodel;


import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class ActivityLoginModel extends BaseObservable {
    ActivityLoginBinding mActivityLoginBinding;

    public ActivityLoginModel(ActivityLoginBinding activityLoginBinding) {
        this.mActivityLoginBinding = activityLoginBinding;
    }

    /**
     * 登录按钮点击事件
     *
     * @param v
     */
    public void login(View v) {
        //TODO 具体的登录逻辑，等服务端相关接口完成以后再实现
        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentHomeActivity);

//        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
//        String pin = mActivityLoginBinding.etActivityLoginVerificationCode.getText().toString();
//        LoginManager.checkPhoneVerificationCode(phoenNum, pin);


    }

    public void sendPhoneVerificationCode(View v) {
        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
        LogKit.v(phoenNum);
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(phoenNum);

    }
}
