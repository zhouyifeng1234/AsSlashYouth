package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidServiceBinding;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.MyBidServiceModel;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMyBidServiceBinding activityMyBidServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_bid_service);
        MyBidServiceModel myBidServiceModel = new MyBidServiceModel(activityMyBidServiceBinding, this);
        activityMyBidServiceBinding.setMyBidServiceModel(myBidServiceModel);
    }
}
