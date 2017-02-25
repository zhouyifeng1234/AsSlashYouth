package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityTransactionRecordBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.TransactionRecoreModel;

/**
 * Created by zss on 2016/11/6.
 */
public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ActivityTransactionRecordBinding activityTransactionRecordBinding;
    private String titleString = "交易记录";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTransactionRecordBinding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_record);
        TransactionRecoreModel transactionRecoreModel = new TransactionRecoreModel(activityTransactionRecordBinding);
        activityTransactionRecordBinding.setTransactionRecoreModel(transactionRecoreModel);
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
