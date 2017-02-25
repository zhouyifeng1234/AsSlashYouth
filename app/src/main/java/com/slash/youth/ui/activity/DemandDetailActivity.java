package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.DemandDetailModel;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDemandDetailBinding activityDemandDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_demand_detail);
        DemandDetailModel demandDetailModel = new DemandDetailModel(activityDemandDetailBinding, this);
        activityDemandDetailBinding.setDemandDetailModel(demandDetailModel);

    }
}
