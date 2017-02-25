package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidServiceBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyBidServiceModel;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceActivity extends BaseActivity {

    public final static int activityRequestCode = 5555;
    public final static int activityResultCode=6666;//代表操作成功

    public MyBidServiceModel mMyBidServiceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMyBidServiceBinding activityMyBidServiceBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_bid_service);
        mMyBidServiceModel = new MyBidServiceModel(activityMyBidServiceBinding, this);
        activityMyBidServiceBinding.setMyBidServiceModel(mMyBidServiceModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activityRequestCode) {
            mMyBidServiceModel.reloadData(false);
        }
    }
}
