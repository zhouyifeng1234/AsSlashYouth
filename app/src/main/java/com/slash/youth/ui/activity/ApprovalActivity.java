package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ApprovalModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityApprovalBinding activityApprovalBinding;
    private Bitmap bitmap;
    private int careertype;
    private ApprovalModel approvalModel;
    private String titleText = "认证";
    // private String submint = "提交";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        careertype = intent.getIntExtra("careertype", 1);
        long uid = intent.getLongExtra("Uid", -1);
        activityApprovalBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval);
        approvalModel = new ApprovalModel(activityApprovalBinding,careertype, this,uid);
        activityApprovalBinding.setApprovalModel(approvalModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleText);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setVisibility(View.GONE);
       // save.setOnClickListener(this);
      //  save.setText(submint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_RETURE);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.APPROVAL_TYPE:
                approvalModel.initData();
                break;
        }
    }
}
