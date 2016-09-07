package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.ui.viewmodel.ActivityCityLocationModel;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class CityLocationActivity extends Activity {

    private ActivityCityLocationBinding mActivityCityLocationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCityLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_location);
        ActivityCityLocationModel activityCityLocationModel = new ActivityCityLocationModel(mActivityCityLocationBinding);
        mActivityCityLocationBinding.setActivityCityLocationModel(activityCityLocationModel);

        initData();
    }

    private void initData() {
        //城市名称模拟数据
        ArrayList<LocationCityInfo> listCityInfo = new ArrayList<LocationCityInfo>();
        listCityInfo.add(new LocationCityInfo(false, "S", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "S", "苏州"));
        //城市名称首字母模拟数据
        ArrayList<Character> listCityNameFirstLetter = new ArrayList<Character>();
        for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityNameFirstLetter.add(cha);
        }

        mActivityCityLocationBinding.lvActivityCityLocationCityinfo.setAdapter(new LocationCityInfoAdapter(listCityInfo));
        mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.setAdapter(new LocationCityFirstLetterAdapter(listCityNameFirstLetter));
    }
}
