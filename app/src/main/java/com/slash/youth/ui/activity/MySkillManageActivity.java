package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMySkillManageBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zss on 2016/11/3.
 */
public class MySkillManageActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMySkillManageBinding activityMySkillManageBinding;
    private TextView title;
    private TextView save;
    private FrameLayout fl;
    private String titleName;
    private MySkillManageModel mySkillManageModel;
    private int skillListId=-1;
    private int i = 0;
    private int offset = 0;
    private int limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        titleName = intent.getStringExtra("Title");
        String avater = intent.getStringExtra("avater");
        String name = intent.getStringExtra("name");
        activityMySkillManageBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_skill_manage);
        mySkillManageModel = new MySkillManageModel(activityMySkillManageBinding,this,intent.getStringExtra("Title"),avater,name);
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
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SKILL_AGREEMENT_ADD_SKILL);

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
        switch (resultCode){
            case Constants.SUMBIT_ONE_SKILL_MANAGER://创建新的模版
                mySkillManageModel.skillManageList.clear();
                mySkillManageModel.getdata(titleName);
                 break;
            case Constants.UPDATE_SKILL_MANAGER_ONE://修改模板
                mySkillManageModel.skillManageList.clear();
                mySkillManageModel.getdata(titleName);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
