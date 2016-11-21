package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyAccountBinding;
import com.slash.youth.ui.viewmodel.MyAccountModel;
import com.slash.youth.ui.viewmodel.SheildModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class MyAccountActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityMyAccountBinding activityMyAccountBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);
        MyAccountModel myAccountModel = new MyAccountModel(activityMyAccountBinding);
        activityMyAccountBinding.setMyAccountModel(myAccountModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("我的账户");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText("交易记录");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("交易记录");
                Intent intentTransactionRecordActivity = new Intent(CommonUtils.getContext(), TransactionRecordActivity.class);
                intentTransactionRecordActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentTransactionRecordActivity);
                break;
        }


    }
}
