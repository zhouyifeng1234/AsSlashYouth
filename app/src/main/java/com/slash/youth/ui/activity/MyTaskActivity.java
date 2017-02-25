package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyTaskBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyTaskModel;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskActivity extends BaseActivity {

    private MyTaskModel mMyTaskModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActivityMyTaskBinding activityMyTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_task);
        mMyTaskModel = new MyTaskModel(activityMyTaskBinding, this);
        activityMyTaskBinding.setMyTaskModel(mMyTaskModel);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyTaskModel.REFRESH_TASK_LIST_STATUS) {
            mMyTaskModel.backRefreshTaskListStatus();
        }
    }
}
