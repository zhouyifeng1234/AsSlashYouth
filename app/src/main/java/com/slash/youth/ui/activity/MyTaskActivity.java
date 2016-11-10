package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyTaskBinding;
import com.slash.youth.ui.viewmodel.MyTaskModel;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);

        ActivityMyTaskBinding activityMyTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_task);
        MyTaskModel myTaskModel = new MyTaskModel(activityMyTaskBinding, this);
        activityMyTaskBinding.setMyTaskModel(myTaskModel);

    }
}
