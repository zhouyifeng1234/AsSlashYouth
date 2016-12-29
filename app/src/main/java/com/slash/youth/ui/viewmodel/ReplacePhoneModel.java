package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
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

    //点击验证
    public void validate(View view) {
        String phoneNumber = activityReplacePhoneBinding.etActivityLoginVerificationPhone.getText().toString();
        if (!phoneNumber.isEmpty()) {
            LoginManager.getPhoneVerificationCode(new onGetPhoneVerificationCode(),phoneNumber);
        } else {
            ToastUtils.shortCenterToast("未填写手机号码");
        }
    }

    //获取验证码
    public class onGetPhoneVerificationCode implements BaseProtocol.IResultExecutor<SendPinResultBean> {
        @Override
        public void execute(SendPinResultBean dataBean) {
            ToastUtils.shortToast("获取验证码成功");
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
            ToastUtils.shortToast("获取验证码失败");
        }
    }
}
