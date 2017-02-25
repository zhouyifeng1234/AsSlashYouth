package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySheildBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.SheildModel;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class SheildActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivitySheildBinding activitySheildBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySheildBinding = DataBindingUtil.setContentView(this, R.layout.activity_sheild);
        SheildModel sheildModel = new SheildModel(activitySheildBinding);
        activitySheildBinding.setSheildModel(sheildModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("屏蔽");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText("移除");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                LogKit.d("移除");

              /*  Intent intentMyAddSkillActivity = new Intent(CommonUtils.getContext(), MyAddSkillActivity.class);
                intentMyAddSkillActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentMyAddSkillActivity);*/

                break;
        }
    }
}
