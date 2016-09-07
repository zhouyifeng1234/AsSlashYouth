package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityCityLocationBinding;

/**
 * Created by zhouyifeng on 2016/9/7.
 */
public class ActivityCityLocationModel extends BaseObservable {
    ActivityCityLocationBinding mActivityCityLocationBinding;

    public ActivityCityLocationModel(ActivityCityLocationBinding activityCityLocationBinding) {
        this.mActivityCityLocationBinding = activityCityLocationBinding;
    }

    private int cityInfoListVisible = View.VISIBLE;
    private int searchCityListVisible = View.INVISIBLE;

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
}
