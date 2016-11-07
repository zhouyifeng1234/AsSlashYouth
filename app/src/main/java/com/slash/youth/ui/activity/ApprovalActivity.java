package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.ui.viewmodel.ActivityUserInfoModel;
import com.slash.youth.ui.viewmodel.ApprovalModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityApprovalBinding activityApprovalBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityApprovalBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval);
        ApprovalModel approvalModel = new ApprovalModel(activityApprovalBinding);
        activityApprovalBinding.setApprovalModel(approvalModel);
        listener();

    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("认证");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText("提交");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("提交审核页面");//examine
                Intent intentExamineActivity = new Intent(CommonUtils.getContext(), ExamineActivity.class);
                intentExamineActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentExamineActivity);
                break;
        }

    }
}
