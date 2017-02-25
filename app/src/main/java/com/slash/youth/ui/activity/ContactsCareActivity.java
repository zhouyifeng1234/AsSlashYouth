package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityContactsCareModel;

/**
 * Created by zss on 2016/11/18.
 */
public class ContactsCareActivity extends BaseActivity {

    private ActivityContactsCareBinding activityContactsCareBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int type = intent.getIntExtra("type", -1);
        activityContactsCareBinding = DataBindingUtil.setContentView(this, R.layout.activity_contacts_care);
        ActivityContactsCareModel activityContactsCareModel = new ActivityContactsCareModel(activityContactsCareBinding,title,this,type);
        activityContactsCareBinding.setActivityContactsCareModel(activityContactsCareModel);
        initView();
    }

    private void initView() {
        activityContactsCareBinding.ivUserinfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
