package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishServiceBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyPublishServiceModel;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyPublishServiceActivity extends BaseActivity {

    public final static int activityRequestCode = 5555;
    public final static int activityResultCode = 6666;//代表操作成功

    public MyPublishServiceModel mMyPublishServiceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyPublishServiceBinding activityMyPublishServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_publish_service);
        mMyPublishServiceModel = new MyPublishServiceModel(activityMyPublishServiceBinding, this);
        activityMyPublishServiceBinding.setMyPublishServiceModel(mMyPublishServiceModel);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activityRequestCode) {
            mMyPublishServiceModel.reloadData(false);
        }
    }
}
