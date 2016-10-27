package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidDemandBinding;
import com.slash.youth.ui.viewmodel.MyBidDemandModel;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyBidDemandActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyBidDemandBinding activityMyBidDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_bid_demand);
        MyBidDemandModel myBidDemandModel = new MyBidDemandModel(activityMyBidDemandBinding, this);
        activityMyBidDemandBinding.setMyBidDemandModel(myBidDemandModel);
    }
}
