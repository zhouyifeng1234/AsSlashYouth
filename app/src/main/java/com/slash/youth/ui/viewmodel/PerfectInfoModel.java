package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.PhoneLoginResultBean;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.test.RichTextTestActivity;
import com.slash.youth.ui.activity.test.RoundedImageTestActivity;
import com.slash.youth.ui.activity.test.ScaleViewPagerTestActivity;
import com.slash.youth.ui.activity.test.TestActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoModel extends BaseObservable {

    ActivityPerfectInfoBinding mActivityPerfectInfoBinding;
    Activity mActivity;
    Bundle thirdPlatformBundle;
    private String _3ptoken;
    private String userInfo;
    boolean isThirdLogin;

    public PerfectInfoModel(ActivityPerfectInfoBinding activityPerfectInfoBinding, Activity activity) {
        this.mActivityPerfectInfoBinding = activityPerfectInfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        thirdPlatformBundle = mActivity.getIntent().getExtras();
        if (thirdPlatformBundle == null) {
            //表示是由手机号登录进入的，所以不再需要输入手机号，隐藏手机号输入框
            setPhonenumLoginInfoVisibility(View.GONE);
            isThirdLogin = false;
        } else {
            isThirdLogin = true;
            _3ptoken = thirdPlatformBundle.getString("_3ptoken");
            userInfo = thirdPlatformBundle.getString("userInfo");
        }
    }

    private void initView() {

    }

    /**
     * 发送验证码
     *
     * @param v
     */
    public void sendPin(View v) {
        String phoenNum = mActivityPerfectInfoBinding.etActivityPerfectInfoPhonenum.getText().toString();
        if (TextUtils.isEmpty(phoenNum)) {
            ToastUtils.shortToast("请输入手机号");
            return;
        }
        LogKit.v(phoenNum);
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(new BaseProtocol.IResultExecutor<SendPinResultBean>() {
            @Override
            public void execute(SendPinResultBean dataBean) {
                ToastUtils.shortToast("获取验证码成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取验证码失败");
            }
        }, phoenNum);
    }

    public void openCamera(View v) {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mActivity.startActivityForResult(intentCamera, 0);
    }

    String phonenum;
    String pin;
    String realname;

    public void okPerfectInfo(View v) {
        phonenum = mActivityPerfectInfoBinding.etActivityPerfectInfoPhonenum.getText().toString();
        pin = mActivityPerfectInfoBinding.etActivityPerfectInfoVerificationCode.getText().toString();
        realname = mActivityPerfectInfoBinding.etActivityPerfectInfoRealname.getText().toString();

        if (isThirdLogin) {//三方登录
            //再次调用手机号登录接口
            if (TextUtils.isEmpty(phonenum)) {
                ToastUtils.shortToast("手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(pin) || TextUtils.isEmpty(realname)) {
                ToastUtils.shortToast("验证码不能为空");
                return;
            }
            if (TextUtils.isEmpty(realname)) {
                ToastUtils.shortToast("真实姓名不能为空");
                return;
            }

            LoginManager.phoneLogin(new BaseProtocol.IResultExecutor<PhoneLoginResultBean>() {
                @Override
                public void execute(PhoneLoginResultBean dataBean) {
                    //如果登录失败，dataBean.data可能是null  {  "rescode": 7  }
                    if (dataBean.data == null) {
                        ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                        return;
                    }
                    String rongToken = dataBean.data.rongToken;//融云token
                    String token = dataBean.data.token;
                    long uid = dataBean.data.uid;
                    if (dataBean.rescode == 0) {
                        //登陆成功，老用户,这里rescode肯定不会11
                        savaLoginState(uid, token, rongToken);
                        setRealname();
                    } else {
                        ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                    }
                }

                @Override
                public void executeResultError(String result) {
                    //这里不会执行
                }
            }, phonenum, pin, _3ptoken, userInfo);

        } else {//手机号登录
            //设置真实姓名
            if (TextUtils.isEmpty(realname)) {
                ToastUtils.shortToast("真实姓名不能为空");
                return;
            }
            setRealname();
        }
    }

    private void setRealname() {
        LoginManager.loginSetRealname(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
                mActivity.startActivity(intentChooseSkillActivity);
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("设置真实姓名失败:" + result);
                ToastUtils.shortToast("设置真实姓名失败:" + result);
            }
        }, realname);
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


    public void openTestActivity(View v) {
        Intent intentTestActivity = new Intent(CommonUtils.getContext(), TestActivity.class);
        intentTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentTestActivity);
    }

    public void openScaleViewPagerTestActivity(View v) {
        Intent intentScaleViewPagerTestActivity = new Intent(CommonUtils.getContext(), ScaleViewPagerTestActivity.class);
        intentScaleViewPagerTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentScaleViewPagerTestActivity);
    }

    public void openRoundedImageTestActivity(View v) {
        Intent intentRoundedImageTestActivity = new Intent(CommonUtils.getContext(), RoundedImageTestActivity.class);
        intentRoundedImageTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentRoundedImageTestActivity);
    }

    public void openRichTextTestActivity(View v) {
        Intent intentRichTextTestActivity = new Intent(CommonUtils.getContext(), RichTextTestActivity.class);
        intentRichTextTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentRichTextTestActivity);
    }

    private int cameraIconVisibility = View.VISIBLE;
    private int phonenumLoginInfoVisibility = View.VISIBLE;

    @Bindable
    public int getPhonenumLoginInfoVisibility() {
        return phonenumLoginInfoVisibility;
    }

    public void setPhonenumLoginInfoVisibility(int phonenumLoginInfoVisibility) {
        this.phonenumLoginInfoVisibility = phonenumLoginInfoVisibility;
        notifyPropertyChanged(BR.phonenumLoginInfoVisibility);
    }

    @Bindable
    public int getCameraIconVisibility() {
        return cameraIconVisibility;
    }

    public void setCameraIconVisibility(int cameraIconVisibility) {
        this.cameraIconVisibility = cameraIconVisibility;
        notifyPropertyChanged(BR.cameraIconVisibility);
    }
}
