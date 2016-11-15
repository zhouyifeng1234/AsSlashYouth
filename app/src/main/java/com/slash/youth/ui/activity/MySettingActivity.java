package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMySettingBinding;
import com.slash.youth.ui.viewmodel.MyAddSkillModel;
import com.slash.youth.ui.viewmodel.MySettingModel;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

/**
 * Created by acer on 2016/11/3.
 */
public class MySettingActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private FrameLayout fl;
    private ActivityMySettingBinding activityMySettingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_setting);
        MySettingModel mySettingModel = new MySettingModel(activityMySettingBinding,this);
        activityMySettingBinding.setMySettingModel(mySettingModel);


        listener();

    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("设置");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                activityMySettingBinding.viewRevise.setVisibility(View.VISIBLE);
                activityMySettingBinding.rlRevise.setVisibility(View.VISIBLE);
                activityMySettingBinding.tvSetAndfindPassword.setText("找回交易密码");
                SpUtils.setBoolean("create_ok",true);
            }
        }
    }
}
