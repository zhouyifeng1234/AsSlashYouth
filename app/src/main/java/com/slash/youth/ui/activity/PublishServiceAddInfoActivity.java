package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceAddinfoBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishServiceAddInfoModel;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceAddInfoActivity extends BaseActivity {

    private PublishServiceAddInfoModel mPublishServiceAddInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPublishServiceAddinfoBinding activityPublishServiceAddinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_addinfo);
        mPublishServiceAddInfoModel = new PublishServiceAddInfoModel(activityPublishServiceAddinfoBinding, this);
        activityPublishServiceAddinfoBinding.setPublishServiceAddInfoModel(mPublishServiceAddInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 10) {
            mPublishServiceAddInfoModel.mSallSkillLabels.getAddLabelsResult(data);
        }
        if (requestCode == 20 && resultCode == 20) {
            String mCurrentAddress = data.getStringExtra("mCurrentAddress");
            String mCurrentAoiName = data.getStringExtra("mCurrentAoiName");
            Double lng = data.getDoubleExtra("lng", 0);
            Double lat = data.getDoubleExtra("lat", 0);
            mPublishServiceAddInfoModel.setLocationInfo(mCurrentAddress,mCurrentAoiName, lng, lat);
        }
    }
}
