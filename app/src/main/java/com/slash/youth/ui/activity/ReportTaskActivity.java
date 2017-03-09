package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReportTaskBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ReportTaskModel;

/**
 * Created by zhouyifeng on 2017/3/9.
 */
public class ReportTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReportTaskBinding activityReportTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_report_task);
        ReportTaskModel reportTaskModel = new ReportTaskModel(activityReportTaskBinding, this);
        activityReportTaskBinding.setReportTaskModel(reportTaskModel);
    }
}
