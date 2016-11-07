package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.ui.viewmodel.MyAddSkillModel;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillActivity extends Activity implements View.OnClickListener {

    private ActivityMyAddSkillBinding activityMyAddSkillBinding;
    private TextView title;
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMyAddSkillBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_add_skill);
        MyAddSkillModel myAddSkillModel = new MyAddSkillModel(activityMyAddSkillBinding);
        activityMyAddSkillBinding.setMyAddSkillModel(myAddSkillModel);
        listener();

    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("添加技能");
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
