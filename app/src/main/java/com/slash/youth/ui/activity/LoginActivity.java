package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.ui.viewmodel.ActivityLoginModel;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ActivityLoginModel activityLoginModel = new ActivityLoginModel(activityLoginBinding);
        activityLoginBinding.setActivityLoginModel(activityLoginModel);
    }
}
