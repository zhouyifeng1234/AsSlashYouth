package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFirstPagerMoreBinding;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.FirstPagerDemandModel;

/**
 * Created by zss on 2016/11/1.
 */
public class FirstPagerMoreActivity extends BaseActivity {
    private ActivityFirstPagerMoreBinding activityFirstPagerMoreBinding;
    private static final String  TITLE ="更多服务";
    public static int barHeight;
    private FirstPagerDemandModel firstPagerDemandModel;
    private CityHistoryEntityDao cityHistoryEntityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化访问最近城市数据库
        cityHistoryEntityDao = SlashApplication.getInstances().getDaoSession().getCityHistoryEntityDao();

        Intent intent = getIntent();
        boolean isDemand = intent.getBooleanExtra("isDemand", false);
        activityFirstPagerMoreBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_pager_more);
        activityFirstPagerMoreBinding.rlBar.measure(0,0);
        barHeight = activityFirstPagerMoreBinding.rlBar.getMeasuredHeight();
        if(!isDemand){
            activityFirstPagerMoreBinding.tvFirstPagerTitle.setText(TITLE);
        }
        firstPagerDemandModel = new FirstPagerDemandModel(activityFirstPagerMoreBinding,isDemand,this,cityHistoryEntityDao);
        activityFirstPagerMoreBinding.setFirstPagerDemandModel(firstPagerDemandModel);
    }
}
