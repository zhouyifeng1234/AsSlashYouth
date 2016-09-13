package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.ui.viewmodel.ActivityMapModel;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class MapActivity extends Activity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
    //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

//        CommonUtils.setCurrentActivity(this);
        ActivityMapBinding activityMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        ActivityMapModel activityMapModel = new ActivityMapModel(activityMapBinding);
        activityMapBinding.setActivityMapModel(activityMapModel);
    }
}
