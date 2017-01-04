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
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.ui.adapter.MySkillManageAdapter;
import com.slash.youth.ui.viewmodel.ActivityHomeModel;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageActivity extends Activity implements View.OnClickListener {
    private ActivityMySkillManageBinding activityMySkillManageBinding;
    private TextView title;
    private TextView save;
    private FrameLayout fl;
    private String titleName;
    private MySkillManageModel mySkillManageModel;
    private int skillListId=-1;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        titleName = intent.getStringExtra("Title");
        activityMySkillManageBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_skill_manage);
        mySkillManageModel = new MySkillManageModel(activityMySkillManageBinding,this,intent.getStringExtra("Title"));
        activityMySkillManageBinding.setMySkillManageModel(mySkillManageModel);
        listener(titleName);
    }

    private void listener(String titleName) {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        switch (titleName){
            case Constants.MY_TITLE_SKILL_MANAGER:
                setActivityTitle(titleName);
                save = (TextView) findViewById(R.id.tv_userinfo_save);
                save.setOnClickListener(this);
                save.setText(Constants.SKILL_MANAGER_TITLE_RIGHT);
                SpUtils.setString("myActivityTitle",titleName);
            break;

            case Constants.MY_TITLE_MANAGER_MY_PUBLISH:
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
                jumpMyAddSkillActivity(this,skillListId);
                break;
        }
    }

    public void jumpMyAddSkillActivity(Activity activity,long skillListId) {
        Intent intentMyAddSkillActivity = new Intent(CommonUtils.getContext(), MyAddSkillActivity.class);
        intentMyAddSkillActivity.putExtra("skillListId",skillListId);
        intentMyAddSkillActivity.putExtra("skillTemplteType",Constants.ADD_ONE_SKILL_MANAGER);
        activity.startActivityForResult(intentMyAddSkillActivity, Constants.ADD_ONE_SKILL_MANAGER);
    }

    private void setActivityTitle(String titleName) {
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Constants.SUMBIT_ONE_SKILL_MANAGER){
            SkillManagerBean.DataBean.ListBean sumbitNewTemplet = (SkillManagerBean.DataBean.ListBean) data.getSerializableExtra("sumbitNewTemplet");
            mySkillManageModel.skillManageList.add(i,sumbitNewTemplet);
            MySkillManageAdapter  mySkillManageAdapter = new MySkillManageAdapter(mySkillManageModel.skillManageList,this,mySkillManageModel.skillManageList);
            activityMySkillManageBinding.lv.setAdapter(mySkillManageAdapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
