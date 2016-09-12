package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.ui.viewmodel.PerfectInfoModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        ActivityPerfectInfoBinding activityPerfectInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_info);
        PerfectInfoModel perfectInfoModel = new PerfectInfoModel(activityPerfectInfoBinding);
        activityPerfectInfoBinding.setPerfectInfoModel(perfectInfoModel);
    }
}
