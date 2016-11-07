package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.ui.viewmodel.FindPassWordModel;
import com.slash.youth.ui.viewmodel.MySettingModel;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView save;
    private ActivityFindPasswordBinding activityFindPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFindPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        FindPassWordModel findPassWordModel = new FindPassWordModel(activityFindPasswordBinding);
        activityFindPasswordBinding.setFindPassWordModel(findPassWordModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("找回密码");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText("提交");
        save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("提交");

                break;
        }
    }

}
