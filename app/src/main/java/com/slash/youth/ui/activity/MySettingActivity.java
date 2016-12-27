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
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.http.protocol.BaseProtocol;
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
    private String titleString ="设置";
    private String findPassWord = "找回交易密码";
    private String setPassWord = "设置交易密码";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testPassWord();
        activityMySettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_setting);
        MySettingModel mySettingModel = new MySettingModel(activityMySettingBinding,this);
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

    //判断是否有交易密码
    private void testPassWord() {
        AccountManager.getTradePasswordStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 1) {//设置了交易密码
                    activityMySettingBinding.viewRevise.setVisibility(View.VISIBLE);
                    activityMySettingBinding.rlRevise.setVisibility(View.VISIBLE);
                    activityMySettingBinding.tvSetAndfindPassword.setText(findPassWord);
                } else if (dataBean.data.status == 0) {//表示当前没有交易密码
                    activityMySettingBinding.viewRevise.setVisibility(View.GONE);
                    activityMySettingBinding.rlRevise.setVisibility(View.GONE);
                    activityMySettingBinding.tvSetAndfindPassword.setText(setPassWord);
                } else {
                    LogKit.v("状态异常");
                }
            }

            @Override
            public void executeResultError(String result) {
            }
        });
    }
}
