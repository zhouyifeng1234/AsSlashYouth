package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.ui.viewmodel.MyAddSkillModel;
import com.slash.youth.ui.viewmodel.MySkillManageModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillActivity extends Activity implements View.OnClickListener {

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
        activityMyAddSkillBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_add_skill);
        myAddSkillModel = new MyAddSkillModel(activityMyAddSkillBinding,this,skillListId);
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
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Constants.SKILL_MANAGER_ADD_LABEL){
            switch (requestCode){
                case Constants.SKILL_MANAGER_ADD_LABEL://获取添加的技能标签
                        myAddSkillModel.sallAddedSkilllabels.getAddLabelsResult(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
