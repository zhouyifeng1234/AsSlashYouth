package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidDemandBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyBidDemandModel;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyBidDemandActivity extends BaseActivity {

    public final static int activityRequestCode = 5555;
    public final static int activityResultCode = 6666;//代表操作成功

    public MyBidDemandModel mMyBidDemandModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMyBidDemandBinding activityMyBidDemandBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_bid_demand);
        mMyBidDemandModel = new MyBidDemandModel(activityMyBidDemandBinding, this);
        activityMyBidDemandBinding.setMyBidDemandModel(mMyBidDemandModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activityRequestCode) {
            mMyBidDemandModel.reloadData(false);
        }
    }

}
