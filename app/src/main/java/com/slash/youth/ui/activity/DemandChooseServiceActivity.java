package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandChooseServiceBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.DemandChooseServiceModel;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class DemandChooseServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDemandChooseServiceBinding activityDemandChooseServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_demand_choose_service);
        DemandChooseServiceModel demandChooseServiceModel = new DemandChooseServiceModel(activityDemandChooseServiceBinding, this);
        activityDemandChooseServiceBinding.setDemandChooseServiceModel(demandChooseServiceModel);
    }
}
