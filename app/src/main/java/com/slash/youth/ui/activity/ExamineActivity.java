package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityExamineCertificatesBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ExamineCertificatesModel;

/**
 * Created by zss on 2016/11/6.
 */
public class ExamineActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ActivityExamineCertificatesBinding activityExamineCertificatesBinding;
    private int type;
    private int cardType;
    private String titleString = "认证";
    private String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent!=null) {
            type = intent.getIntExtra("careertype", -1);
            cardType = intent.getIntExtra("cardType", -1);
            photoUri = intent.getStringExtra("photoUri");
        }
        activityExamineCertificatesBinding = DataBindingUtil.setContentView(this, R.layout.activity_examine_certificates);
        ExamineCertificatesModel examineCertificatesModel = new ExamineCertificatesModel(activityExamineCertificatesBinding,this,type,cardType,photoUri);
        activityExamineCertificatesBinding.setExamineCertificatesModel(examineCertificatesModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
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
