package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyAddSkillModel;
import com.slash.youth.utils.Constants;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMyAddSkillBinding activityMyAddSkillBinding;
    private TextView title;
    private FrameLayout fl;
    private ArrayList<String> listCheckedLabelName;
    private MyAddSkillModel myAddSkillModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long skillListId = intent.getLongExtra("skillListId", -1);
        int skillTemplteType = intent.getIntExtra("skillTemplteType", -1);
        int position = intent.getIntExtra("position", -1);
        activityMyAddSkillBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_add_skill);
        myAddSkillModel = new MyAddSkillModel(activityMyAddSkillBinding, this, skillListId,skillTemplteType,position);
        activityMyAddSkillBinding.setMyAddSkillModel(myAddSkillModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(Constants.SKILL_MANAGER_TITLE_RIGHT);
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.SKILL_MANAGER_ADD_LABEL) {
            switch (requestCode) {
                case Constants.SKILL_MANAGER_ADD_LABEL://获取添加的技能标签
                    myAddSkillModel.sallAddedSkilllabels.listTotalAddedTagsNames.clear();
                    myAddSkillModel.sallAddedSkilllabels.listTotalAddedTags.clear();
                    myAddSkillModel.sallAddedSkilllabels.removeAllViews();
                    myAddSkillModel.sallAddedSkilllabels.getAddLabelsResult(data);

                    break;
            }
        }
        myAddSkillModel.addPic.addPicResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
