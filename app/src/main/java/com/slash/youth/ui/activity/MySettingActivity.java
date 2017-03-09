package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMySettingBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MySettingModel;

/**
 * Created by acer on 2016/11/3.
 */
public class MySettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private FrameLayout fl;
    private ActivityMySettingBinding activityMySettingBinding;
    private String titleString ="设置";

    private MySettingModel mySettingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_setting);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mySettingModel = new MySettingModel(activityMySettingBinding,this);
        activityMySettingBinding.setMySettingModel(mySettingModel);
        listener();

    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
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
