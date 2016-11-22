package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.ui.viewmodel.ReplacePhoneModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/11/2.
 */
public class ReplacePhoneActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityReplacePhoneBinding activityReplacePhoneBinding;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReplacePhoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_replace_phone);
        ReplacePhoneModel replacePhoneModel = new ReplacePhoneModel(activityReplacePhoneBinding);
        activityReplacePhoneBinding.setReplacePhoneModel(replacePhoneModel);
        intent = new Intent();
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("填写新手机号码");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText("完成");
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_save:
                String phone = activityReplacePhoneBinding.etActivityLoginVerificationPhone.getText().toString();
                String code = activityReplacePhoneBinding.etActivityLoginVerificationCode.getText().toString();
                if(code.isEmpty()){
                  //  ToastUtils.shortCenterToast("未填写验证码");
                }
                if(phone.isEmpty()){
                   // ToastUtils.shortToast("未填写手机号码");
                }
                if(!phone.isEmpty()&&!code.isEmpty()){
                    //保存手机号码
                    LoginManager.checkPhoneVerificationCode(phone,code);
                    intent.putExtra("phone",phone);
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;
        }
    }

}
