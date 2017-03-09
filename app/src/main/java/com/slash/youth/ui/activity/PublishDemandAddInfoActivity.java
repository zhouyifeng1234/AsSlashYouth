package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishDemandAddInfoModel;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoActivity extends BaseActivity {

    private PublishDemandAddInfoModel mPublishDemandAddInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_addinfo);
        mPublishDemandAddInfoModel = new PublishDemandAddInfoModel(activityPublishDemandAddinfoBinding, this);
        activityPublishDemandAddinfoBinding.setPublishDemandAddInfoModel(mPublishDemandAddInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 10) {
            mPublishDemandAddInfoModel.mSallSkillLabels.getAddLabelsResult(data);
        }
        if (requestCode == 20 && resultCode == 20) {
            String mCurrentAddress = data.getStringExtra("mCurrentAddress");
            String mCurrentAoiName = data.getStringExtra("mCurrentAoiName");
            Double lng = data.getDoubleExtra("lng", 0);
            Double lat = data.getDoubleExtra("lat", 0);
            mPublishDemandAddInfoModel.setLocationAddress(mCurrentAddress, mCurrentAoiName, lng, lat);
        }
    }
}
