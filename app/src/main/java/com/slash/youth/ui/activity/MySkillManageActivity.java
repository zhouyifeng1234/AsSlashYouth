package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityHomeBinding;
import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageActivity extends Activity implements View.OnClickListener {

    private ActivityMySkillManageBinding activityMySkillManageBinding;
    private TextView title;
    private TextView save;
    private FrameLayout fl;
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        titleName = intent.getStringExtra("Title");

        activityMySkillManageBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_skill_manage);
        MySkillManageModel mySkillManageModel = new MySkillManageModel(activityMySkillManageBinding,this,intent.getStringExtra("Title"));
        activityMySkillManageBinding.setMySkillManageModel(mySkillManageModel);
        listener(titleName);

    }
    private void listener(String titleName) {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);

        switch (titleName){
            case "技能管理":
                setActivityTitle(titleName);
                save = (TextView) findViewById(R.id.tv_userinfo_save);
                save.setOnClickListener(this);
                save.setText("添加技能");
                SpUtils.setString("myActivityTitle",titleName);
            break;

            case "管理我发布的任务":
                setActivityTitle(titleName);
                fl = (FrameLayout) findViewById(R.id.fl_title_right);
                fl.setVisibility(View.GONE);
                SpUtils.setString("myActivityTitle",titleName);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
                case R.id.iv_userinfo_back:
                    finish();
                    break;

            case R.id.tv_userinfo_save:
                LogKit.d("添加技能");
                Intent intentMyAddSkillActivity = new Intent(CommonUtils.getContext(), MyAddSkillActivity.class);
                intentMyAddSkillActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentMyAddSkillActivity);
                break;
        }
    }

    private void setActivityTitle(String titleName) {

        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleName);
    }



}
