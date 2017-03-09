package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PhoneNumUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by acer on 2016/11/2.
 */
public class ReplacePhoneModel extends BaseObservable {

    private ActivityReplacePhoneBinding activityReplacePhoneBinding;

    public ReplacePhoneModel(ActivityReplacePhoneBinding activityReplacePhoneBinding) {
        this.activityReplacePhoneBinding = activityReplacePhoneBinding;
        listener();
    }

    private void listener() {

    }

    boolean isSendPin = false;
    //点击验证
    public void validate(View view) {
        if (isSendPin) {
            return;
        }

        isSendPin = true;
        String phoneNumber = activityReplacePhoneBinding.etActivityLoginVerificationPhone.getText().toString();
       
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.shortCenterToast("未填写手机号码");
            return;
        }
        boolean isCorrect = PhoneNumUtils.checkPhoneNum(phoneNumber);
        if (!isCorrect) {
            ToastUtils.shortToast("请输入正确的手机号码");
            return;
        }

        LoginManager.getPhoneVerificationCode(new onGetPhoneVerificationCode(), phoneNumber);
    }
    private int pinSecondsCount;

    //获取验证码
    public class onGetPhoneVerificationCode implements BaseProtocol.IResultExecutor<SendPinResultBean> {
        @Override
        public void execute(SendPinResultBean dataBean) {
            ToastUtils.shortToast("验证码已发送，请查收");
            isSendPin = true;
            pinSecondsCount = 61;
            CommonUtils.getHandler().post(new PinCountDown());
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
            ToastUtils.shortToast("获取验证码失败");
        }
    }

    private class PinCountDown implements Runnable {
        @Override
        public void run() {
            pinSecondsCount--;
            if (pinSecondsCount < 0) {
                activityReplacePhoneBinding.btnSendpinText.setText("验证码");
                isSendPin = false;
            } else {
                activityReplacePhoneBinding.btnSendpinText.setText(pinSecondsCount + "S");
                CommonUtils.getHandler().postDelayed(this, 1000);
            }
        }
    }
}
