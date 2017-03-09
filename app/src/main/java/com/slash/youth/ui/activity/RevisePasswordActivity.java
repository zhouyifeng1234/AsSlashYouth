package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityRevisePasswordBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.RevisePassWordModel;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/11/3.
 */
public class RevisePasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityRevisePasswordBinding activityRevisePasswordBinding;
    private RevisePassWordModel revisePassWordModel;
    private String titleString = "修改交易密码";
    private String sure = "确定";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRevisePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_revise_password);
        revisePassWordModel = new RevisePassWordModel(activityRevisePasswordBinding, this);
        activityRevisePasswordBinding.setRevisePassWordModel(revisePassWordModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText(sure);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_save:
                String oldpass = revisePassWordModel.oldPassWordMap.get("oldpass");
                String newpass = revisePassWordModel.newPassWordMap.get("newpass");
                String surepass = revisePassWordModel.surePassWordMap.get("surepass");
                if (!TextUtils.isEmpty(oldpass) && !TextUtils.isEmpty(newpass) && !TextUtils.isEmpty(surepass)) {
                    if (newpass.equals(surepass)) {
                        revisePassWordModel.setNewPassWord(oldpass, newpass);
                    } else {
                        ToastUtils.shortToast("输入的新密码不一致,请重新输入");
                    }

                } else {
                    ToastUtils.shortToast("请填写完整");
                }
//                finish();
                break;
        }
    }
}
