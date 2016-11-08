package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityRevisePasswordBinding;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.ui.viewmodel.RevisePassWordModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class RevisePasswordActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityRevisePasswordBinding activityRevisePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRevisePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_revise_password);
        RevisePassWordModel revisePassWordModel = new RevisePassWordModel(activityRevisePasswordBinding);
        activityRevisePasswordBinding.setRevisePassWordModel(revisePassWordModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("修改交易密码");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText("确定");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("确定");

                break;
        }
    }

}
