package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.LayoutWithdrawalsBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.WithdrawalsModel;

/**
 * Created by zss on 2016/11/6.
 */
public class WithdrawalsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private LayoutWithdrawalsBinding layoutWithdrawalsBinding;
    private String titleText ="提现";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String currentMoney = intent.getStringExtra("currentMoney");
        layoutWithdrawalsBinding = DataBindingUtil.setContentView(this, R.layout.layout_withdrawals);
        WithdrawalsModel withdrawalsModel = new WithdrawalsModel(layoutWithdrawalsBinding,this,currentMoney);
        layoutWithdrawalsBinding.setWithdrawalsModel(withdrawalsModel);

        listener();
    }
    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleText);
        findViewById(R.id.fl_title_right).setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }

}
