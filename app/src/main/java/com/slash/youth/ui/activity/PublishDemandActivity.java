package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishDemandModel;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PublishDemandActivity extends BaseActivity {

    private PublishDemandModel mPublishDemandModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishDemandBinding activityPublishDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand);
        mPublishDemandModel = new PublishDemandModel(activityPublishDemandBinding, this);
        activityPublishDemandBinding.setPublishDemandModel(mPublishDemandModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        ToastUtils.shortToast("onActivityResult " + requestCode + " " + resultCode);
        if (requestCode == 10 && resultCode == 10) {
            mPublishDemandModel.mSallSkillLabels.getAddLabelsResult(data);
        }
    }
}
