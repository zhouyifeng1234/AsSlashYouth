package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.ui.viewmodel.ActivityCityLocationModel;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class CityLocationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCityLocationBinding activityCityLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_location);
        ActivityCityLocationModel activityCityLocationModel = new ActivityCityLocationModel(activityCityLocationBinding);
        activityCityLocationBinding.setActivityCityLocationModel(activityCityLocationModel);
    }
}
