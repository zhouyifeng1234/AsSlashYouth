package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.DemandDetailModel;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailActivity extends BaseActivity {

    private DemandDetailModel mDemandDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDemandDetailBinding activityDemandDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_demand_detail);
        mDemandDetailModel = new DemandDetailModel(activityDemandDetailBinding, this);
        activityDemandDetailBinding.setDemandDetailModel(mDemandDetailModel);

    }

    @Override
    public void onBackPressed() {
        int viewPicVisibility = mDemandDetailModel.getViewPicVisibility();
        if (viewPicVisibility == View.VISIBLE) {
            mDemandDetailModel.setViewPicVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
