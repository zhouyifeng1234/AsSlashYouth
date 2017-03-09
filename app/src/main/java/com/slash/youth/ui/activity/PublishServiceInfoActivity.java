package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceInfoBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishServiceInfoModel;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceInfoActivity extends BaseActivity {

    private PublishServiceInfoModel mPublishServiceInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishServiceInfoBinding activityPublishServiceInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_info);
        mPublishServiceInfoModel = new PublishServiceInfoModel(activityPublishServiceInfoBinding, this);
        activityPublishServiceInfoBinding.setPublishServiceInfoModel(mPublishServiceInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 10) {
            mPublishServiceInfoModel.mSallSkillLabels.getAddLabelsResult(data);
        }
        mPublishServiceInfoModel.mSaplAddPic.addPicResult(requestCode, resultCode, data);
    }
}
