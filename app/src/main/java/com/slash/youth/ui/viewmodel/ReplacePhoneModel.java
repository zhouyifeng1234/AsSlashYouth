package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.engine.LoginManager;
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
            LoginManager.getPhoneVerificationCode(phoneNumber);
        } else {
            ToastUtils.shortCenterToast("未填写手机号码");
        }
    }
}
