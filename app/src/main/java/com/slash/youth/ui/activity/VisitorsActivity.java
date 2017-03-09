package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.databinding.ActivityVisitorsBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityContactsCareModel;
import com.slash.youth.ui.viewmodel.VisitorsCareModel;

/**
 * Created by zss on 2016/11/18.
 */
public class VisitorsActivity extends BaseActivity {

    private ActivityVisitorsBinding activityContactsCareBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        activityContactsCareBinding = DataBindingUtil.setContentView(this, R.layout.activity_visitors);
        VisitorsCareModel activityContactsCareModel = new VisitorsCareModel(activityContactsCareBinding, "最近访客", this);
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
