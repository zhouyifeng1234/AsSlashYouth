package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.test.RichTextTestActivity;
import com.slash.youth.ui.activity.test.RoundedImageTestActivity;
import com.slash.youth.ui.activity.test.ScaleViewPagerTestActivity;
import com.slash.youth.ui.activity.test.TestActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoModel extends BaseObservable {

    ActivityPerfectInfoBinding mActivityPerfectInfoBinding;
    Activity mActivity;

    public PerfectInfoModel(ActivityPerfectInfoBinding activityPerfectInfoBinding, Activity activity) {
        this.mActivityPerfectInfoBinding = activityPerfectInfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

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


    public void okPerfectInfo(View v) {
        //再次调用手机号登录接口

        //设置真实姓名

//        Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
//        intentChooseSkillActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentChooseSkillActivity);
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

    private int cameraIconVisibility;

    @Bindable
    public int getCameraIconVisibility() {
        return cameraIconVisibility;
    }

    public void setCameraIconVisibility(int cameraIconVisibility) {
        this.cameraIconVisibility = cameraIconVisibility;
        notifyPropertyChanged(BR.cameraIconVisibility);
    }
}
