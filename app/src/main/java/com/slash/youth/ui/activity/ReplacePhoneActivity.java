package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ReplacePhoneModel;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/11/2.
 */
public class ReplacePhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityReplacePhoneBinding activityReplacePhoneBinding;
    private Intent intent;
    private String titleString = "填写新手机号码";
    private String finish = "完成";
    private String phone;
    private String code;

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
        title.setText(titleString);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText(finish);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_save:
                phone = activityReplacePhoneBinding.etActivityLoginVerificationPhone.getText().toString();
                code = activityReplacePhoneBinding.etActivityLoginVerificationCode.getText().toString();
                if(code.isEmpty()){
                    ToastUtils.shortCenterToast("未填写验证码");
                }
                if(phone.isEmpty()){
                    ToastUtils.shortToast("未填写手机号码");
                }
                if(!phone.isEmpty()&&!code.isEmpty()){
                    //保存手机号码
                    LoginManager.UpdatePhoneVerificationCodeProtocol(new onCheckPhoneVerificationCode(), phone, code);
                }
                finish();
                break;
        }
    }

    //验证手机验证码
    public class onCheckPhoneVerificationCode implements BaseProtocol.IResultExecutor<SendPinResultBean> {
        @Override
        public void execute(SendPinResultBean dataBean) {
            int rescode = dataBean.rescode;
            switch (rescode){
                case 0:
                    intent.putExtra("phone",phone);
                    setResult(RESULT_OK,intent);
                    break;
                case 1:
                    LogKit.d("返回失败");
                    break;
                case 2:
                    LogKit.d("参数错误");
                    break;
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
            ToastUtils.shortToast("验证码不正确，请重新验证");
        }
    }
}
