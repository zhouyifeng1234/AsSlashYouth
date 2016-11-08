package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReplacePhoneBinding;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;
import com.slash.youth.ui.viewmodel.ReplacePhoneModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/2.
 */
public class ReplacePhoneActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityReplacePhoneBinding activityReplacePhoneBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityReplacePhoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_replace_phone);
        ReplacePhoneModel replacePhoneModel = new ReplacePhoneModel(activityReplacePhoneBinding);
        activityReplacePhoneBinding.setReplacePhoneModel(replacePhoneModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("更换手机号码");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText("下一步");
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("下一步");
                Intent intentFinishPhoneActivity = new Intent(CommonUtils.getContext(), FinishPhoneActivity.class);
                intentFinishPhoneActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentFinishPhoneActivity);
                break;
        }
    }

}
