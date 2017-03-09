package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFinishPhoneBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.FinishPhoneModel;
import com.slash.youth.utils.LogKit;

/**
 * Created by acer on 2016/11/2.
 */
public class FinishPhoneActivity extends BaseActivity implements View.OnClickListener {

    private ActivityFinishPhoneBinding activityFinishPhoneBinding;
    private TextView title;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFinishPhoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_finish_phone);
        FinishPhoneModel finishPhoneModel = new FinishPhoneModel(activityFinishPhoneBinding);
        activityFinishPhoneBinding.setFinishPhoneModel(finishPhoneModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("填写新号码");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText("完成");
        save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("完成页面");

                break;
        }
    }
}
