package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityExamineCertificatesBinding;
import com.slash.youth.ui.viewmodel.ApprovalModel;
import com.slash.youth.ui.viewmodel.ExamineCertificatesModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class ExamineActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private ActivityExamineCertificatesBinding activityExamineCertificatesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityExamineCertificatesBinding = DataBindingUtil.setContentView(this, R.layout.activity_examine_certificates);
        ExamineCertificatesModel examineCertificatesModel = new ExamineCertificatesModel(activityExamineCertificatesBinding);
        activityExamineCertificatesBinding.setExamineCertificatesModel(examineCertificatesModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("认证");
        findViewById(R.id.fl_title_right).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_userinfo_back:
                finish();
                break;

        }




    }
}
