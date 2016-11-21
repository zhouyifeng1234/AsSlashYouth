package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReportTaBinding;
import com.slash.youth.ui.viewmodel.ReportTAModel;

/**
 * Created by zss on 2016/11/2.
 */
public class ReportTAActivity extends Activity implements View.OnClickListener {

    private ActivityReportTaBinding activityReportTaBinding;
    private TextView title;
    private TextView save;
    private ReportTAModel reportTAModel;
    private int uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", -1);

    activityReportTaBinding = DataBindingUtil.setContentView(this, R.layout.activity_report_ta);
    reportTAModel = new ReportTAModel(activityReportTaBinding,uid);
    activityReportTaBinding.setReportTAModel(reportTAModel);

    listener();
    }


    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("举报");
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
                reportTAModel.sendData();
                finish();
                break;
        }

    }

}
