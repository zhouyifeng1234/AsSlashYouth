package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityCityLocationBinding;
import com.slash.youth.ui.activity.CityLocationActivity;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class ActivityCityLocationModel extends BaseObservable {
    ActivityCityLocationBinding mActivityCityLocationBinding;
    CityLocationActivity mCityLocationActivity;

    public ActivityCityLocationModel(ActivityCityLocationBinding activityCityLocationBinding, CityLocationActivity cityLocationActivity) {
        this.mActivityCityLocationBinding = activityCityLocationBinding;
        this.mCityLocationActivity = cityLocationActivity;
    }

    public int cityInfoListVisible = View.VISIBLE;
    public int searchCityListVisible = View.INVISIBLE;

    @Bindable
    public int getCityInfoListVisible() {
        return cityInfoListVisible;
    }

    public void setCityInfoListVisible(int cityInfoListVisible) {
        this.cityInfoListVisible = cityInfoListVisible;
        notifyPropertyChanged(BR.cityInfoListVisible);
    }

    @Bindable
    public int getSearchCityListVisible() {
        return searchCityListVisible;
    }

    public void setSearchCityListVisible(int searchCityListVisible) {
        this.searchCityListVisible = searchCityListVisible;
        notifyPropertyChanged(BR.searchCityListVisible);
    }

    public void finishCityLocationActivity(View v){
        mCityLocationActivity.finish();
    }


    //清除搜索内容
    public void cleanEt(View v){
        mActivityCityLocationBinding.etActivityCityLocationSearchbox.setText(null);
    }


}
