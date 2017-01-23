package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishServiceBinding;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.MyPublishServiceModel;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyPublishServiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyPublishServiceBinding activityMyPublishServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_publish_service);
        MyPublishServiceModel myPublishServiceModel = new MyPublishServiceModel(activityMyPublishServiceBinding, this);
        activityMyPublishServiceBinding.setMyPublishServiceModel(myPublishServiceModel);
    }

}
