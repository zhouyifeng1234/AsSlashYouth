package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyPublishDemandModel;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyPublishDemandActivity extends BaseActivity {

    public final static int activityRequestCode = 5555;
    public final static int activityResultCode = 6666;//代表操作成功

    public MyPublishDemandModel mMyPublishDemandModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyPublishDemandBinding activityMyPublishDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_publish_demand);
        mMyPublishDemandModel = new MyPublishDemandModel(activityMyPublishDemandBinding, this);
        activityMyPublishDemandBinding.setMyPublishDemandModel(mMyPublishDemandModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activityRequestCode) {
            mMyPublishDemandModel.reloadData(false);
        }
    }
}
