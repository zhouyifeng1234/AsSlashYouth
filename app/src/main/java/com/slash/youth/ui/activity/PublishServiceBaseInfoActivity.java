package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishServiceBaseInfoModel;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceBaseInfoActivity extends BaseActivity {

    private PublishServiceBaseInfoModel mPublishServiceBaseInfoModel;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_baseinfo);
        mPublishServiceBaseInfoModel = new PublishServiceBaseInfoModel(activityPublishServiceBaseinfoBinding, this);
        activityPublishServiceBaseinfoBinding.setPublishServiceBaseInfoModel(mPublishServiceBaseInfoModel);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPublishServiceBaseInfoModel.mSaplAddPic.addPicResult(requestCode, resultCode, data);
    }

}
