package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyAccountBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyAccountModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityMyAccountBinding activityMyAccountBinding;
    private String titleTextAccount = "我的账户";
    private String titleTextRecord = "交易记录";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        MyAccountModel myAccountModel = new MyAccountModel(activityMyAccountBinding,this);
        activityMyAccountBinding.setMyAccountModel(myAccountModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleTextAccount);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText(titleTextRecord);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_save:
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_ACCOUNT_DEAL_RECORD);

                Intent intentTransactionRecordActivity = new Intent(CommonUtils.getContext(), TransactionRecordActivity.class);
                intentTransactionRecordActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentTransactionRecordActivity);
                break;
        }
    }
}
